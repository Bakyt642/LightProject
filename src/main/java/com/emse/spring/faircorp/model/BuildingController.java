package com.emse.spring.faircorp.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController  // (1)
@CrossOrigin
@RequestMapping("/api/buildings") // (2)
@Transactional // (3)
public class BuildingController {

    @Autowired
    private final BuildingDao buildingDao; // (4)

    public BuildingController(BuildingDao dao){
        this.buildingDao = dao;
    }

    @GetMapping // (5)
    public List<BuildingDto> findAll() {
        return buildingDao.findAll()
                .stream()
                .map(BuildingDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public BuildingDto findById(@PathVariable Long id) {
        return buildingDao.findById(id).map(building -> new BuildingDto(building)).orElse(null);
    }

    @PostMapping
    public BuildingDto create(@RequestBody BuildingDto dto) {
        Building building = null;
        if (dto.getId() != null) {
            building = buildingDao.findById(dto.getId()).orElse(null);
        }

        if (building == null) {
            building = buildingDao.save(new Building(dto.getName()));
        } else {
            building.setName(dto.getName());

            buildingDao.save(building);
        }

        return new BuildingDto(building);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        buildingDao.deleteById(id);
    }
}

