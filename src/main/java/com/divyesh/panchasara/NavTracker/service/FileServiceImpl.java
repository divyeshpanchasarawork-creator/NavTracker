package com.divyesh.panchasara.NavTracker.service;

import com.divyesh.panchasara.NavTracker.entity.FundEntity;
import com.divyesh.panchasara.NavTracker.repository.FundRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class FileServiceImpl implements FileService{

    private final FundRepository fundRepository;
    private static final DateTimeFormatter NAV_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

    public FileServiceImpl(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    @Override
    @Transactional
    public void processFile(String filePath) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String firstLine = br.readLine();

            if (firstLine == null || firstLine.isBlank()) {
                throw new IOException("File is empty");
            }

            String[] header = getHeader(firstLine);

            List<FundEntity> funds = readData(br, header.length);

            for (FundEntity fund : funds) {
                if (!fundRepository.existsBySchemeCodeAndNavDate(fund.getSchemeCode(), fund.getNavDate())) {
                    fundRepository.save(fund);
                }
            }
        } finally {
            deleteFile(filePath);
        }
    }

    private void deleteFile(String path) throws RuntimeException {
        File file = new File(path);

        if (!file.delete()) {
            throw new RuntimeException("Failed to delete file: " + path);
        }
    }

    private String[] getHeader(String line) {
        line = line.replace(" ", "");
        line = line.replace("/", "_");
        return line.split(";");
    }

    private List<FundEntity> readData(BufferedReader bufferedReader, int length) throws IOException {
        List<FundEntity> fundEntityList = new ArrayList<>();

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (!line.isBlank() && line.contains(";")) {
                String[] data = line.split(";", -1);

                if (data.length == length) {
                    fundEntityList.add(convertDataToEntity(data));
                }
            }
        }

        return fundEntityList;
    }

    private FundEntity convertDataToEntity(String[] data) {
        FundEntity fundEntity = new FundEntity();

        fundEntity.setSchemeCode(nullIfBlankOrDash(data[0]));
        fundEntity.setIsinDivPayoutGrowth(nullIfBlankOrDash(data[1]));
        fundEntity.setIsinDivReinvestment(nullIfBlankOrDash(data[2]));
        fundEntity.setSchemeName(nullIfBlankOrDash(data[3]));

        String nav = nullIfBlankOrDash(data[4]);
        if (nav != null) {
            fundEntity.setNetAssetValue(Double.parseDouble(nav));
        }

        String navDate = nullIfBlankOrDash(data[5]);
        if (navDate != null) {
            fundEntity.setNavDate(
                    LocalDate.parse(navDate, NAV_DATE_FORMATTER)
            );
        }

        return fundEntity;
    }

    private String nullIfBlankOrDash(String value) {
        if (value == null) {
            return null;
        }

        value = value.trim();

        return value.isEmpty() || "-".equals(value) ? null : value;
    }
}
