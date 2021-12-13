package org.qualitified.reporting.adhoc.operation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.util.DocumentHelper;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.runtime.api.Framework;

import java.io.IOException;


@Operation(id= DuplicateDashboard.ID, category= Constants.CAT_EXECUTION, label="DuplicateDashboard", description="")
public class DuplicateDashboard {
    public static final String ID="Qualitified.DuplicateDashboard";
    private Log logger = LogFactory.getLog(DuplicateDashboard.class);

    public static final String DC_CREATOR = "dc:creator";

    public static final String DC_CONTRIBUTORS = "dc:contributors";

    public static final String DC_LASTCONTRIBUTOR = "dc:lastContributor";

    public static final String UID_MAJORVERSION = "uid:major_version";

    public static final String UID_MINORVERSION = "uid:minor_version";

    @Context
    protected CoreSession session;
    @OperationMethod()
    public void run() throws IOException {

        DocumentModelList reportList = session.query("SELECT * FROM Document WHERE ecm:primaryType in ('ChartBar', 'ChartLine', 'ChartPie', 'Count', 'Table', 'List') AND ecm:isTrashed = 0 AND ecm:isVersion = 0 " +
                "AND ecm:currentLifeCycleState != 'deleted' AND dc:creator = 'Administrator' ORDER BY dc:created ASC");
        UserWorkspaceService uws = Framework.getService(UserWorkspaceService.class);
        DocumentModel home = uws.getUserPersonalWorkspace(session.getPrincipal().getName(), session.getRootDocument());
        for (DocumentModel doc: reportList) {
            DocumentModel newDoc =session.createDocumentModel(home.getPathAsString(), doc.getName(), doc.getType());
            newDoc.copyContent(doc);
            Properties properties = new Properties();

            String creator = session.getPrincipal().getName();
            properties.put(DC_CREATOR, session.getPrincipal().getName());
            properties.put(DC_CONTRIBUTORS, creator);
            properties.put(UID_MINORVERSION, "0");
            properties.put(UID_MAJORVERSION, "0");

            DocumentHelper.setProperties(session, newDoc, properties);
            session.createDocument(newDoc);
            session.save();
        }

    }
}








