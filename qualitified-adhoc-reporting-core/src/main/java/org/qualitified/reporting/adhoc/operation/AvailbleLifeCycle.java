package org.qualitified.reporting.adhoc.operation;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.lifecycle.LifeCycle;
import org.nuxeo.ecm.core.lifecycle.LifeCycleService;
import org.nuxeo.ecm.core.lifecycle.LifeCycleState;
import org.nuxeo.runtime.api.Framework;
import java.util.Collection;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;


@Operation(id= AvailbleLifeCycle.ID, category=Constants.CAT_DOCUMENT, label="list life cycle states ", description="for each doctype selected get all life cycle states.")
public class AvailbleLifeCycle {
    public static final String ID = "LifeCycleEntries";


    @Param(name="docTypeSelection")
    protected String docTypeSelection;

    private Log logger = LogFactory.getLog(GetOptionValue.class);

    @OperationMethod
    public Blob run() throws IOException  {
        List<Map<String, Object>> result = new ArrayList<>();
            logger.warn(docTypeSelection);
            LifeCycleService service = Framework.getService(LifeCycleService.class);
            String NameLife = service.getLifeCycleNameFor(docTypeSelection);

            LifeCycle NameCycle = service.getLifeCycleByName(NameLife);
            Collection<LifeCycleState> ListLife = NameCycle.getStates();
            //ListLife.stream().map(item->result.add((String)item.getName()))
            if (!ListLife.isEmpty()) {
                for (LifeCycleState cs : ListLife){
                    Map<String, Object> suggestionJSON = new LinkedHashMap<>();
                    suggestionJSON.put("id", cs.getName());
                    suggestionJSON.put("displayLabel", cs.getName());
                    result.add(suggestionJSON);
                }
            }


        return Blobs.createJSONBlobFromValue(result);
    }


}
