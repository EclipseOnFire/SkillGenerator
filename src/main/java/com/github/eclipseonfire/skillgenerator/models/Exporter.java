package com.github.eclipseonfire.skillgenerator.models;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * A utility class that exports {@link Skill}s.
 *
 * @author Arthur MILLE
 */
public final class Exporter {

    private Exporter() {}

    public static final int TYPE_HTML = 0;
    public static final int TYPE_CSV  = 1;

    public static void export(int type, Collection<Skill> skills, Path targetFile) throws IOException {
        switch (type) {
            case TYPE_HTML:
                exportHtml(skills, targetFile);
                break;
            case TYPE_CSV:
                exportCsv(skills, targetFile);
                break;
            default:
                throw new IllegalArgumentException("Wrong parameter type!");
        }
    }

    private static void exportHtml(Collection<Skill> skills, Path targetFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {

            writer.write("<table><thead><tr><th>Code</th><th>Libellé</th></tr></thead><tbody>");

            for (Skill skill : skills) {
                writer
                        .append("<tr><td>")
                        .append(skill.getCode())
                        .append("</td><td>")
                        .append(skill.getLabel())
                        .append("</td></tr>");
            }

            writer.write("</tbody></table>");
        }
    }

    private static void exportCsv(Collection<Skill> skills, Path targetFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(targetFile, Charset.forName("Windows-1252"))) {

            writer.write("Code;Libellé\r\n");

            for (Skill skill : skills) {
                writer.append(skill.getCode()).append(';').append(skill.getLabel()).append("\r\n");
            }

            writer.flush();
        }
    }
}