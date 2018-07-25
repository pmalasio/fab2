package eu.cehj.cdb2.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import eu.cehj.cdb2.business.service.db.HubReferenceService;
import eu.cehj.cdb2.common.dto.HubReferenceDTO;
import eu.cehj.cdb2.hub.service.CompetenceService;

@RestController
@RequestMapping("api/reference")
public class ReferenceController extends BaseController {

    @Autowired
    private HubReferenceService hubReferenceService;

    @Autowired
    private CompetenceService competenceService;

    @RequestMapping(method = GET)
    @ResponseStatus(value = OK)
    public HubReferenceDTO get(){
        return this.hubReferenceService.getReference();
    }

    @RequestMapping(method = GET, value = "competences")
    @ResponseStatus(value = OK)
    public CompetenceService getCompetences() {

        return  this.competenceService;
    }
}