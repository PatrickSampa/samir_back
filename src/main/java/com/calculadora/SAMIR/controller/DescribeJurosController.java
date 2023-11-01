package com.calculadora.SAMIR.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.calculadora.SAMIR.DTO.DescribeJurosDTO;
import com.calculadora.SAMIR.services.DescribeJurosService;

@RestController
@CrossOrigin
@RequestMapping("/describeJuros")
public class DescribeJurosController {
    
    @Autowired
    private DescribeJurosService describeJurosService;

    @GetMapping
    public @ResponseBody ResponseEntity<Object> findAll(){
        try {
            List<DescribeJurosDTO> describeCorrecaoList = describeJurosService.findAll();
            return ResponseEntity.ok(describeCorrecaoList);            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Object> save(@RequestBody DescribeJurosDTO describeJurosDTO){
        try {
            return ResponseEntity.ok().body(describeJurosService.save(describeJurosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public @ResponseBody ResponseEntity<Object> update(@RequestBody DescribeJurosDTO describeJurosDTO){
        try {
            return ResponseEntity.ok().body(describeJurosService.update(describeJurosDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public @ResponseBody ResponseEntity<Object> delete(@PathVariable int id){
        try {
            describeJurosService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
}
