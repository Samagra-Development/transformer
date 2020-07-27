package com.samagra.transformer.odk.repository;

import com.samagra.transformer.odk.entity.GupshupStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<GupshupStateEntity, Long> {
  GupshupStateEntity findByPhoneNo(String phoneNo);
  GupshupStateEntity findByPhoneNoAndBotFormName(String phoneNo, String botFormName);
}
