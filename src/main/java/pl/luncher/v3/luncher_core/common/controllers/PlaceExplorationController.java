package pl.luncher.v3.luncher_core.common.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "places-exploration", description = "Places exploration (end-user)")
@RestController
@RequestMapping("/places/exploration")
@RequiredArgsConstructor
public class PlaceExplorationController {
    
}
