package com.data.repository;


import com.data.dao.BData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BDataRepository extends CrudRepository<BData,Integer> {
    public List<BData> findAll();
}
