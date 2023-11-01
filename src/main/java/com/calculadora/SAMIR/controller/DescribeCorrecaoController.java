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

import com.calculadora.SAMIR.DTO.DescribeCorrecaoDTO;
import com.calculadora.SAMIR.entities.DescribeCorrecao;
import com.calculadora.SAMIR.services.DescribeCorrecaoService;


@RestController
@CrossOrigin
@RequestMapping("/describeCorrecao")
public class DescribeCorrecaoController {
    @Autowired
    private DescribeCorrecaoService DescribeCorrecaoService; 

    @GetMapping
    public ResponseEntity<Object> findAll(){
        try {
            List<DescribeCorrecaoDTO> describeCorrecaoList = DescribeCorrecaoService.findAll();
            return ResponseEntity.ok(describeCorrecaoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{type}")
    public @ResponseBody ResponseEntity<Object> findById(@PathVariable int type){
        try {
            return ResponseEntity.ok().body(DescribeCorrecaoService.findByType(type));            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Object> save(@RequestBody DescribeCorrecaoDTO describeCorrecaoDTO){
        try {
            return ResponseEntity.ok().body(DescribeCorrecaoService.save(describeCorrecaoDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public @ResponseBody ResponseEntity<Object> update(@RequestBody DescribeCorrecao describeCorrecao){
        try {
            return ResponseEntity.ok().body(DescribeCorrecaoService.update(describeCorrecao));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Object> delete(@PathVariable int id){
        try {
            DescribeCorrecaoService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
