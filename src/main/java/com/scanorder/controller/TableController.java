package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.entity.DiningTable;
import com.scanorder.repository.DiningTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/tables", "/api/tables"})
public class TableController {

    @Autowired
    private DiningTableRepository tableRepository;

    @GetMapping
    public Result<List<DiningTable>> getTables() {
        return Result.success(tableRepository.findAll());
    }

    @PutMapping("/{id}/status")
    public Result<DiningTable> updateTableStatus(@PathVariable(name = "id") String id, @RequestBody Map<String, String> body) {
        return tableRepository.findById(id).map(table -> {
            if (body.containsKey("status")) table.setStatus(body.get("status"));
            if (body.containsKey("currentOrderId")) table.setCurrentOrderId(body.get("currentOrderId"));
            tableRepository.save(table);
            return Result.success(table);
        }).orElse(Result.error("桌台不存在"));
    }
}
