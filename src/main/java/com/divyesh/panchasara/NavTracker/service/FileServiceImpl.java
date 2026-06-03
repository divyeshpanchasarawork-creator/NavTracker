package com.divyesh.panchasara.NavTracker.service;

import com.divyesh.panchasara.NavTracker.entity.FundEntity;
import com.divyesh.panchasara.NavTracker.repository.FundRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FileServiceImpl implements FileService{

    private final FundRepository fundRepository;
    private static final DateTimeFormatter NAV_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

    private static final String COL_SCHEME_CODE        = "schemecode";
    private static final String COL_SCHEME_NAME        = "schemename";
    private static final String COL_ISIN_DIV_PAYOUT    = "isindivpayout_isingrowth";
    private static final String COL_ISIN_DIV_REINVEST  = "isindivreinvestment";
    private static final String COL_NAV                = "netassetvalue";
    private static final String COL_DATE               = "date";

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

            Map<String, Integer> headerMapping = createHeaderMapping(header);

            List<FundEntity> funds = readData(br, header.length, headerMapping);

            for (FundEntity fund : funds) {
                if (!fundRepository.existsBySchemeCodeAndNavDate(fund.getSchemeCode(), fund.getNavDate())) {
                    fundRepository.save(fund);
                }
            }
        } finally {
            deleteFile(filePath);
        }
    }

    private Map<String, Integer> createHeaderMapping(String[] header) {
        Map<String, Integer> headerMapping = new HashMap<>();

        for (int i = 0; i < header.length; i++) headerMapping.put(header[i], i);

        return headerMapping;
    }

    private void deleteFile(String path) throws RuntimeException {
        File file = new File(path);

        if (!file.delete()) {
            throw new RuntimeException("Failed to delete file: " + path);
        }
    }

    private String[] getHeader(String line) {
        line = line.toLowerCase();
        line = line.replace(" ", "");
        line = line.replace("/", "_");
        return line.split(";");
    }

    private List<FundEntity> readData(BufferedReader bufferedReader, int length, Map<String, Integer> headerMapping) throws IOException {
        List<FundEntity> fundEntityList = new ArrayList<>();

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (!line.isBlank() && line.contains(";")) {
                String[] data = line.split(";", -1);

                if (data.length == length) {
                    fundEntityList.add(convertDataToEntity(data, headerMapping));
                }
            }
        }

        return fundEntityList;
    }

    private FundEntity convertDataToEntity(String[] data, Map<String, Integer> headerMapping) {
        FundEntity fundEntity = new FundEntity();

        fundEntity.setSchemeCode(extract(data, headerMapping, COL_SCHEME_CODE));
        fundEntity.setIsinDivPayoutGrowth(extract(data, headerMapping, COL_ISIN_DIV_PAYOUT));
        fundEntity.setIsinDivReinvestment(extract(data, headerMapping, COL_ISIN_DIV_REINVEST));
        fundEntity.setSchemeName(extract(data, headerMapping, COL_SCHEME_NAME));

        String nav = extract(data, headerMapping, COL_NAV);
        if (nav != null) {
            fundEntity.setNetAssetValue(new BigDecimal(nav));
        }

        String navDate = extract(data, headerMapping, COL_DATE);
        if (navDate != null) {
            fundEntity.setNavDate(LocalDate.parse(navDate, NAV_DATE_FORMATTER));
        }

        return fundEntity;
    }

    private String extract(String[] data, Map<String, Integer> headerMapping, String column) {
        Integer index = headerMapping.get(column);
        if (index == null || index >= data.length) return null;
        return nullIfBlankOrDash(data[index]);
    }

    private String nullIfBlankOrDash(String value) {
        if (value == null) {
            return null;
        }

        value = value.trim();

        return value.isEmpty() || "-".equals(value) ? null : value;
    }
}
