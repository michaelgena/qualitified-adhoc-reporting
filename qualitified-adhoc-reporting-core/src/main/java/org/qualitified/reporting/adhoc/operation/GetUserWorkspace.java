package org.qualitified.reporting.adhoc.operation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.runtime.api.Framework;


@Operation(id= GetUserWorkspace.ID, category= Constants.CAT_EXECUTION, label="GetUserWorkspace", description="")
public class GetUserWorkspace {
    public static final String ID="Qualitified.GetUserWorkspace";
    private Log logger = LogFactory.getLog(GetUserWorkspace.class);

    @Context
    protected CoreSession session;
    @OperationMethod()
    public String run() throws  OperationException {

        UserWorkspaceService uws = Framework.getService(UserWorkspaceService.class);
        DocumentModel home = uws.getUserPersonalWorkspace(session.getPrincipal().getName(), session.getRootDocument());
        return home.getPathAsString();
        }
    }









