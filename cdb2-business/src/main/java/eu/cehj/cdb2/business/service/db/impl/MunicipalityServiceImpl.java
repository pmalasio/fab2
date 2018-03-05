package eu.cehj.cdb2.business.service.db.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;

import eu.cehj.cdb2.business.dao.MunicipalityRepository;
import eu.cehj.cdb2.business.service.data.GeoDataStructure;
import eu.cehj.cdb2.business.service.data.RecordBuilderHelper;
import eu.cehj.cdb2.business.service.db.MunicipalityService;
import eu.cehj.cdb2.common.dto.MunicipalityDTO;
import eu.cehj.cdb2.entity.AdminAreaSubdivisionMajor;
import eu.cehj.cdb2.entity.AdminAreaSubdivisionMiddle;
import eu.cehj.cdb2.entity.AdminAreaSubdivisionMinor;
import eu.cehj.cdb2.entity.Municipality;

@Service
public class MunicipalityServiceImpl extends BaseServiceImpl<Municipality, MunicipalityDTO, Long, MunicipalityRepository> implements MunicipalityService {

    @Autowired
    private EntityManager em;

    @Override
    public MunicipalityDTO save(final MunicipalityDTO municipalityDTO) throws Exception {
        Municipality entity = municipalityDTO.getId() == null ? new Municipality() : this.get(municipalityDTO.getId());

        BeanUtils.copyProperties(municipalityDTO, entity);

        entity = this.save(entity);

        return new MunicipalityDTO(entity);
    }

    @Override
    public List<MunicipalityDTO> getAllDTO() throws Exception {

        final List<Municipality> municipalities = this.repository.findAllByOrderByPostalCode();
        final List<MunicipalityDTO> municipalityDTOs = new ArrayList<>(municipalities.size());
        municipalities.forEach(m -> {
            final MunicipalityDTO dto = new MunicipalityDTO();
            dto.setId(m.getId());
            dto.setName(m.getName());
            dto.setPostalCode(m.getPostalCode());
            final AdminAreaSubdivisionMajor areaMajor = m.getAdminAreaSubdivisionMajor();
            if (areaMajor != null) {
                dto.setAdminAreaSubdivisionMajor(areaMajor.getName());
            }
            final AdminAreaSubdivisionMiddle areaMiddle = m.getAdminAreaSubdivisionMiddle();
            if (areaMiddle != null) {
                dto.setAdminAreaSubdivisionMiddle(areaMiddle.getName());
            }
            final AdminAreaSubdivisionMinor areaMinor = m.getAdminAreaSubdivisionMinor();
            if (areaMinor != null) {
                dto.setAdminAreaSubdivisionMinor(areaMinor.getName());
            }
            municipalityDTOs.add(dto);
        });
        return municipalityDTOs;
    }

    @Override
    public Page<MunicipalityDTO> findAll(final Predicate predicate, final Pageable pageable) throws Exception {
        final Page<Municipality> entities = this.repository.findAll(predicate, pageable);
        final List<MunicipalityDTO> dtos = new ArrayList<>();
        final Iterator<Municipality> it = entities.iterator();
        while (it.hasNext()) {
            dtos.add(this.populateDTOFromEntity(it.next()));
        }
        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }

    @Override
    public void updateAreaFromStructure(final GeoDataStructure structure, final RecordBuilderHelper helper) {
        final Municipality area = this.populateEntity(structure, helper);
        this.repository.save(area);
        helper.setMunicipality(area);
    }

    @Override
    public Municipality populateEntity(final GeoDataStructure structure, final RecordBuilderHelper helper) {
        Municipality area = this.repository.getByPostalCodeAndName(structure.getZipCode(), structure.getCityName());
        if (area == null) {
            area = new Municipality();
        }
        area.setPostalCode(structure.getZipCode());
        area.setName(structure.getCityName());
        area.setLatitude(structure.getxPos());
        area.setLongitude(structure.getyPos());
        area.setAdminAreaSubdivisionMajor(helper.getMajorArea());
        area.setAdminAreaSubdivisionMiddle(helper.getMiddleArea());
        area.setAdminAreaSubdivisionMinor(helper.getMinorArea());
        return area;
    }

    @Override
    public MunicipalityDTO populateDTOFromEntity(final Municipality municipality) {
        final MunicipalityDTO dto = new MunicipalityDTO();
        dto.setId(municipality.getId());
        dto.setName(municipality.getName());
        dto.setPostalCode(municipality.getPostalCode());
        if (municipality.getAdminAreaSubdivisionMajor() != null) {
            dto.setAdminAreaSubdivisionMajor(municipality.getAdminAreaSubdivisionMajor().getName());
        }
        if (municipality.getAdminAreaSubdivisionMiddle() != null) {
            dto.setAdminAreaSubdivisionMiddle(municipality.getAdminAreaSubdivisionMiddle().getName());
        }
        if (municipality.getAdminAreaSubdivisionMinor() != null) {
            dto.setAdminAreaSubdivisionMinor(municipality.getAdminAreaSubdivisionMinor().getName());
        }
        return dto;
    }

    @Override
    public Municipality populateEntityFromDTO(final MunicipalityDTO dto) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Municipality getByPostalCodeAndName(final String postalCode, final String name) throws Exception {
        return this.repository.getByPostalCodeAndName(postalCode, name);
    }

}
