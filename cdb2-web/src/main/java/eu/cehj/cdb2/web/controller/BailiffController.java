package eu.cehj.cdb2.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.types.Predicate;

import static org.springframework.http.HttpStatus.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import eu.cehj.cdb2.business.exception.CDBException;
import eu.cehj.cdb2.business.service.db.BailiffService;
import eu.cehj.cdb2.business.service.db.CDBTaskService;
import eu.cehj.cdb2.common.dto.BailiffDTO;
import eu.cehj.cdb2.common.dto.BailiffExportDTO;
import eu.cehj.cdb2.common.service.StorageService;
import eu.cehj.cdb2.entity.Bailiff;
import eu.cehj.cdb2.entity.CDBTask;
import eu.cehj.cdb2.entity.QBailiff;
import eu.cehj.cdb2.web.service.BailiffImportService;
import eu.cehj.cdb2.web.utils.Settings;

@RestController
@RequestMapping("api/bailiff")
public class BailiffController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BailiffController.class);

    @Autowired
    BailiffService bailiffService;

    @Autowired
    BailiffImportService bailiffImportService;

    @Autowired
    Settings settings;

    @Autowired
    StorageService storageService;

    @Autowired
    CDBTaskService cdbTaskService;

    @Value("${bailiff.import.template.file}")
    Resource bailiffImportTemplate;

    @RequestMapping(
            method = {
                    POST, PUT
            })
    @ResponseStatus(value = CREATED)
    public BailiffDTO save(@RequestBody final BailiffDTO bailiffDTO){

        return this.bailiffService.save(bailiffDTO);
    }

    @RequestMapping(method = GET)
    @ResponseStatus(value = OK)
    public List<BailiffDTO> get(@RequestParam(required = false) final Boolean deleted) {
        if((deleted != null) && (deleted)) {
            return this.bailiffService.getAllEvenDeletedDTO();
        }
        return this.bailiffService.getAllDTO();
    }

    /**
     * Used for xml generation
     */
    @RequestMapping(method = GET, value = "all")
    @ResponseStatus(value = OK)
    public List<BailiffExportDTO> getAllForExport() {
        return this.bailiffService.getAllForExport();
    }

    // We may need this again in a near future : I keep it !
    //    @RequestMapping(method = GET, value = "search")
    //    @ResponseStatus(value = OK)
    //    public Page<BailiffDTO> search(@QuerydslPredicate(root = Bailiff.class) final Predicate predicate, final Pageable pageable) {
    //        // Because we return only active bailiffs, we have to tweak the search from the http request, in order to add deleted filter
    //        final QBailiff bailiff = QBailiff.bailiff;
    //        final Predicate tweakedPredicate = (bailiff.deleted.isFalse().or(bailiff.deleted.isNull())).and(predicate);
    //        return this.bailiffService.findAll(tweakedPredicate, pageable);
    //    }

    @RequestMapping(method = GET, value = "search")
    @ResponseStatus(value = OK)
    public List<BailiffDTO> search(@QuerydslPredicate(root = Bailiff.class) final Predicate predicate, final Pageable pageable){
        // Because we return only active bailiffs, we have to tweak the search from the http request, in order to add deleted filter
        final QBailiff bailiff = QBailiff.bailiff;
        final Predicate tweakedPredicate = (bailiff.deleted.isFalse().or(bailiff.deleted.isNull())).and(predicate);
        return this.bailiffService.findAll(tweakedPredicate, pageable);
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    @ResponseStatus(value = NO_CONTENT)
    public void delete(@PathVariable() final Long id){
        this.bailiffService.delete(id);
    }

    @RequestMapping(method = { POST }, value="import")
    @ResponseStatus(value = HttpStatus.OK)
    public CDBTask importData(@RequestParam("file") final MultipartFile file){
        final CDBTask task = new CDBTask(CDBTask.Type.BAILIFF_IMPORT);
        task.setStatus(CDBTask.Status.STARTED);
        this.cdbTaskService.save(task);
        try {
            this.storageService.store(file);
            this.bailiffImportService.importFile(file.getOriginalFilename(), this.settings.getCountryCode(), task);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(),e);
            throw new CDBException(e.getMessage(),e);
        }
        return task;
    }

    @RequestMapping(method = { GET }, value="export")
    public ResponseEntity<Resource> exportData(){
        //TODO: For now I assume export can only be issued locally, hence we don't need to provide any country code argument. If it appears that Hub can request xls export as well, it will need to be changed.
        try {
            final String exportFilePath = this.bailiffImportService.export(this.settings.getCountryCode());
            final Path path = Paths.get(exportFilePath);
            final ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            LOGGER.debug("headers : " + resource.getFilename());
            return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CDBException(e.getMessage(),e);
        }
    }

    @RequestMapping(method = { GET }, value="template")
    public ResponseEntity<Resource> downloadTemplate(){
        try {
            if(this.bailiffImportTemplate.exists()) {
                try (InputStream is = this.bailiffImportTemplate.getInputStream()) {
                    final byte[] ba = IOUtils.toByteArray(is);
                    final ByteArrayResource bar = new ByteArrayResource(ba);
                    return ResponseEntity.ok()
                            .contentLength(this.bailiffImportTemplate.contentLength())
                            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                            .body(bar);
                }
            }else {
                throw new FileNotFoundException("Bailiff import template file could not be found.");
            }
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(),e);
            throw new CDBException(e.getMessage(),e);
        }

    }

    @RequestMapping(method = GET, value = "/{id}")
    @ResponseStatus(value = OK)
    public BailiffDTO getById(@PathVariable final Long id) {
        return this.bailiffService.getDTO(id);
    }

}