package org.qualitified.reporting.adhoc.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import java.util.GregorianCalendar;

public class CommentPostSaveListener implements EventListener {

    private Log logger = LogFactory.getLog(CommentPostSaveListener.class);

    @Override
    public void handleEvent(Event event) {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        CoreSession session = docCtx.getCoreSession();
        DocumentModel comment = docCtx.getSourceDocument();
        AutomationService automationService = Framework.getService(AutomationService.class);
        OperationContext operationContext = new OperationContext(docCtx.getCoreSession());
        operationContext.setInput(comment);
        if (!("Comment".equals(comment.getType())) || comment.getId() == null) {
            return;
        }
        IdRef parentId = new IdRef((String) comment.getPropertyValue("comment:parentId"));
        GregorianCalendar creationDate = (GregorianCalendar) comment.getPropertyValue("comment:creationDate");
        GregorianCalendar modificationDate = (GregorianCalendar) comment.getPropertyValue("comment:modificationDate");

        DocumentModel commentedDoc = session.getDocument(parentId);

        GregorianCalendar updatedDocDate = (modificationDate != null) ? modificationDate : creationDate;
        commentedDoc.setPropertyValue("dc:modified",updatedDocDate);
        session.saveDocument(commentedDoc);
        /*try {

            boolean isSynced = interactionDoc.getPropertyValue("interaction:toSync") != null && (boolean) interactionDoc.getPropertyValue("interaction:toSync");
            boolean toSend = interactionDoc.getPropertyValue("interaction:toSend") != null && (boolean) interactionDoc.getPropertyValue("interaction:toSend");

            if ( Boolean.TRUE.equals(isSynced)) {
                logger.warn("Running SynchronizeWithCalendar on event "+ interactionDoc.getTitle());
                automationService.run(operationContext, "Qualitified.SynchronizeWithCalendar");
            }
            if ( Boolean.TRUE.equals(toSend)) {
                logger.warn("Running SendEmailFromInteraction on email "+ interactionDoc.getTitle());
                automationService.run(operationContext, "Qualitified.SendEmailFromInteraction");
            }
        } catch (OperationException e) {
            logger.error("Error while running operations...", e);
        }*/
    }
}