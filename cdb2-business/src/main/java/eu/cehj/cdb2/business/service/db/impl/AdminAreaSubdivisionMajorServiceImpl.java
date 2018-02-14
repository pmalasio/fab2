package eu.cehj.cdb2.business.service.db.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.cehj.cdb2.business.dao.AdminAreaSubdivisionMajorRepository;
import eu.cehj.cdb2.business.service.data.GeoDataStructure;
import eu.cehj.cdb2.business.service.data.RecordBuilderHelper;
import eu.cehj.cdb2.business.service.db.AdminAreaSubdivisionMajorService;
import eu.cehj.cdb2.common.dto.AdminAreaSubdivisionMajorDTO;
import eu.cehj.cdb2.entity.AdminAreaSubdivisionMajor;

@Service
public class AdminAreaSubdivisionMajorServiceImpl  extends BaseServiceImpl<AdminAreaSubdivisionMajor, AdminAreaSubdivisionMajorDTO, Long> implements AdminAreaSubdivisionMajorService {

    @Autowired
    private EntityManager em;

    @Autowired
    private AdminAreaSubdivisionMajorRepository repository;

    @Override
    public void updateAreaFromStructure(final GeoDataStructure structure, final RecordBuilderHelper helper) {
        AdminAreaSubdivisionMajor area = this.repository.getByCode(structure.getMajorAreaCode());
        if(area == null) {
            area = this.populateEntity(structure, helper);
            this.repository.save(area);
        }
        helper.setMajorArea(area);
    }

    @Override
    public AdminAreaSubdivisionMajor populateEntity(final GeoDataStructure structure, final RecordBuilderHelper helper) {
        final AdminAreaSubdivisionMajor area = new AdminAreaSubdivisionMajor();
        area.setCode(structure.getMajorAreaCode());
        area.setName(structure.getMajorAreaName());
        return area;
    }

    @Override
    public List<AdminAreaSubdivisionMajorDTO> getAllDTO() throws Exception {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    @Override
    public AdminAreaSubdivisionMajorDTO save(final AdminAreaSubdivisionMajorDTO dto) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AdminAreaSubdivisionMajorDTO getDTO(final Long id) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AdminAreaSubdivisionMajor populateEntityFromDTO(final AdminAreaSubdivisionMajorDTO dto) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AdminAreaSubdivisionMajorDTO populateDTOFromEntity(final AdminAreaSubdivisionMajor entity) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
