package eu.cehj.cdb2.web.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import eu.cehj.cdb2.business.service.db.BailiffService;
import eu.cehj.cdb2.common.dto.BailiffDTO;
import eu.cehj.cdb2.entity.Bailiff;

@RestController
@RequestMapping("api/bailiff")
public class BailiffController extends BaseController {

    @Autowired
    BailiffService bailiffService;

    @RequestMapping(method = { POST, PUT })
    @ResponseStatus(value = CREATED)
    public BailiffDTO save(@RequestBody final BailiffDTO bailiffDTO) throws Exception {

        return this.bailiffService.save(bailiffDTO);
    }

    @RequestMapping(method = { GET })
    @ResponseStatus(value = OK)
    public List<BailiffDTO> get() throws Exception {
        return this.bailiffService.getAllDTO();
    }

    @RequestMapping(method = RequestMethod.GET, value="search")
    @ResponseStatus(value = OK)
    public Page<BailiffDTO> search(
            @QuerydslPredicate(root = Bailiff.class) final Predicate predicate, final Pageable pageable) throws Exception {
        return this.bailiffService.findAll(predicate, pageable);
    }

}