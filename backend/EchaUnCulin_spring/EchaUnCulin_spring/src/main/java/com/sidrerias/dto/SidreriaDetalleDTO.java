package com.sidrerias.dto;

import com.sidrerias.modelo.Review;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Builder
@Data
public class SidreriaDetalleDTO {

   private SidreriaDTO sidreriaDTO;
    private List<ReviewDTO> reviews;

}
