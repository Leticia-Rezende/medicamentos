package org.medicamentos.medicamento.utils;

import java.nio.file.Paths;

public class PathFXML {
    public static String pathBase(){
        return Paths.get("C:\\Users\\Unifan\\medicamentos\\src\\main\\java\\org\\medicamentos\\medicamento\\view").toAbsolutePath().toString();
    }
}

