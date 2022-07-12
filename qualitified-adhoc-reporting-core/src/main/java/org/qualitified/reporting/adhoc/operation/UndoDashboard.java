package org.qualitified.reporting.adhoc.operation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;

import java.io.IOException;


@Operation(id= UndoDashboard.ID, category= Constants.CAT_EXECUTION, label="UndoDashboard", description="")
public class UndoDashboard {
    public static final String ID="Qualitified.UndoDashboard";
    private Log logger = LogFactory.getLog(UndoDashboard.class);

    @Context
    protected CoreSession session;
    @OperationMethod()
    public void run() throws IOException {
        String creator = session.getPrincipal().getName();
        DocumentModelList reportList = session.query("SELECT * FROM Document WHERE ecm:primaryType in ('ChartBar', 'ChartLine', 'ChartPie', 'Count', 'Table', 'List') AND ecm:isTrashed = 0 AND ecm:isVersion = 0 " +
                "AND ecm:currentLifeCycleState != 'deleted' AND dc:creator = '"+creator+"' ORDER BY dc:created ASC");
        for (DocumentModel doc: reportList) {
            session.removeDocument(doc.getRef());
            session.save();
        }
    }
}








