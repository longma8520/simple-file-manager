package cn.tyson.simplefilemanager.repository;

import cn.tyson.simplefilemanager.model.entity.TextFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextFileRepository extends JpaRepository<TextFile, Integer> {

    Page<TextFile> findAll(Pageable pageable);

    int countByFileName(String fileName);
}
