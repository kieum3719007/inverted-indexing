package com.example.demo.TermCounter;

import java.util.List;
import java.util.Map;

public interface TermCounterInterface {
    public Map<String, Integer> valuesCount(List<String> tokens);
}
