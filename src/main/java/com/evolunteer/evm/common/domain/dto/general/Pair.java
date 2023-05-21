package com.evolunteer.evm.common.domain.dto.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data(staticConstructor = "of")
public class Pair <K ,V>{
    private K key;
    public V value;
}
