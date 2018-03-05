package eu.cehj.cdb2.business.service.db.impl;

import org.springframework.stereotype.Service;

import eu.cehj.cdb2.business.dao.CountryOfSyncRepository;
import eu.cehj.cdb2.business.service.db.CountryOfSyncService;
import eu.cehj.cdb2.common.dto.CountryOfSyncDTO;
import eu.cehj.cdb2.entity.CountryOfSync;

@Service
public class CountryOfSyncServiceImpl extends BaseServiceImpl<CountryOfSync, CountryOfSyncDTO, Long, CountryOfSyncRepository> implements CountryOfSyncService {


    @Override
    public CountryOfSync populateEntityFromDTO(final CountryOfSyncDTO dto) throws Exception {
        final CountryOfSync entity = dto.getId() == null ? new CountryOfSync() : this.get(dto.getId());
        entity.setActive(dto.isActive());
        entity.setName(dto.getName());
        entity.setUrl(dto.getUrl());
        entity.setUser(dto.getUser());
        entity.setPassword(dto.getPassword());
        entity.setCountryCode(dto.getCountryCode());
        return entity;
    }

    @Override
    public CountryOfSyncDTO populateDTOFromEntity(final CountryOfSync entity) throws Exception {
        final CountryOfSyncDTO dto = new CountryOfSyncDTO();
        dto.setName(entity.getName());
        dto.setActive(entity.isActive());
        dto.setId(entity.getId());
        // dto.setLastSync(entity.getSynchronizations().); FIXME: add method to get last sync on entity
        dto.setPassword(entity.getPassword());
        dto.setUser(entity.getUser());
        dto.setUrl(entity.getUrl());
        dto.setCountryCode(entity.getCountryCode());
        return dto;
    }

    @Override
    public CountryOfSync getByCountryCode(final String countryCode) throws Exception {
        return this.repository.getByCountryCode(countryCode);
    }




}
