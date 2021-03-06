package eu.cehj.cdb2.business.service.db;

import org.springframework.stereotype.Service;

import eu.cehj.cdb2.common.dto.AdminAreaSubdivisionMajorDTO;
import eu.cehj.cdb2.entity.AdminAreaSubdivisionMajor;

@Service
public interface AdminAreaSubdivisionMajorService extends BaseService<AdminAreaSubdivisionMajor, AdminAreaSubdivisionMajorDTO, Long>, AdminAreaSubdivisionService, BaseGeoService<AdminAreaSubdivisionMajor>{

}