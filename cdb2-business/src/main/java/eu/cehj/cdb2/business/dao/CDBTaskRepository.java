package eu.cehj.cdb2.business.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.cehj.cdb2.entity.CDBTask;


@RepositoryRestResource
public interface CDBTaskRepository extends JpaRepository<CDBTask, Long>, QueryDslPredicateExecutor<CDBTask> {

}
