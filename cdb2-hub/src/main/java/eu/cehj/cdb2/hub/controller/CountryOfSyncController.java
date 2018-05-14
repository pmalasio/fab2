package eu.cehj.cdb2.hub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import eu.cehj.cdb2.business.service.db.CountryOfSyncService;
import eu.cehj.cdb2.common.dto.CountryOfSyncDTO;
import eu.cehj.cdb2.common.dto.CountryOfSyncRefDTO;

@RestController
@RequestMapping("api/country")
public class CountryOfSyncController extends BaseController {


    @Autowired
    CountryOfSyncService countryOfSyncService;

    @RequestMapping(method = { GET })
    @ResponseStatus(value = OK)
    public List<CountryOfSyncDTO> get() {
        return this.countryOfSyncService.getAllDTO();
    }

    @RequestMapping(method = { GET }, value = "refs")
    @ResponseStatus(value = OK)
    public List<CountryOfSyncRefDTO> getRefs() {
        return this.countryOfSyncService.getAllRefDTO();
    }

    @RequestMapping(method = { POST })
    @ResponseStatus(value = CREATED)
    public CountryOfSyncDTO save(@RequestBody final CountryOfSyncDTO dto){
        return this.countryOfSyncService.save(dto);
    }


    @RequestMapping(method = { DELETE }, value = "/{id}")
    @ResponseStatus(value = NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        this.countryOfSyncService.delete(id);
    }

}