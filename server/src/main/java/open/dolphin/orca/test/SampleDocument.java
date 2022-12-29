package open.dolphin.orca.test;

import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.util.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleDocument {

    public static DocumentModel getDocumentModel(String fileName) {
        DocumentModel documentModel = null;
        try {
            Path pref = Paths.get(fileName);
            String json = String.join("", Files.readAllLines(pref));
            documentModel = JsonUtils.fromJson(json, DocumentModel.class);

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return documentModel;
    }

    public static List<RegisteredDiagnosisModel> getRegisteredDiagnosisModel(String fileName) {
        List<RegisteredDiagnosisModel> diagnoses = new ArrayList<>();
        try {
            Path pref = Paths.get(fileName);
            String json = String.join("", Files.readAllLines(pref));
            RegisteredDiagnosisModel[] diags = JsonUtils.fromJson(json, RegisteredDiagnosisModel[].class);
            diagnoses.addAll(Arrays.asList(diags));

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return diagnoses;
    }

    public static void main(String[] argv) {
        //DocumentModel documentModel = SampleDocument.getDocumentModel("SampleDocumentModel.json");
        //System.out.println(JsonUtils.toJson(documentModel));

        List<RegisteredDiagnosisModel> diagnoses = SampleDocument.getRegisteredDiagnosisModel("SampleRegisteredDiagnosisModels.json");
        System.out.println(JsonUtils.toJson(diagnoses));
    }
}
