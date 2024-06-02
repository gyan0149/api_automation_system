package com.example.apiTest.Service;


import com.example.apiTest.Model.SubmitResponse;
import com.example.apiTest.Model.UserReport;
import com.example.apiTest.Repository.UserAccountRepository;
import com.example.apiTest.Repository.UserMdrRepository;
import com.example.apiTest.Repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserMdrRepository userMdrRepository;

    private RestTemplate restTemplate = new RestTemplate();

    public void processApis(String filePath) throws IOException {
        List<String> apis = readApiFile(filePath);
        List<ApiReport> reports = new ArrayList<>();
        System.out.println(apis);

        for (String api : apis) {
           String userId = extractUserId(api);
           System.out.println("UserId: " + userId);
        
           String protrans = extractProtrans(api);
           System.out.println("protrans: " + protrans);

            if (userId != null) {
            	
            	//user Code
                Integer userCode = userRepository.findUserCodeByUserId(userId);
                System.out.println("UserCode:-   "+userCode);
                
                //group permission
                String groupPermission  = userRepository.findGroupPermissionByUserId(userId);
                System.out.println("groupPermission:- "+groupPermission);
                Integer parentUserCode=0;
                BigInteger amountAfterParent=null;
                BigInteger amountBeforeParent=null;
                BigDecimal finalPriceParent = null;
                BigInteger totalDeductionParent= null;
                boolean statusParent= true;
                //Now extract parent userCode
                if (!"superreseller".equals(groupPermission)) {
                 parentUserCode = userRepository.findParentUserCodeByUserId(userId);
                System.out.println("parentUserCode:-   "+parentUserCode);
                if(parentUserCode != null) {
                	String UserIdParent = userRepository.findUserIdByUserCode(parentUserCode);
                	 BigDecimal priceParent;
                	 
                     //protrans price   
                     if(protrans.equals("trans")){
                     	  priceParent = userRepository.findTransUnitPriceByUserId(UserIdParent);
                          System.out.println("priceParent:-  "+priceParent);
                     }
                     else {
                    	 priceParent = userRepository.findPromoUnitPriceByUserId(UserIdParent);
                         System.out.println("priceParent:-  "+priceParent);
                     }
                     
                     //
                     String smsDltPricParent = userRepository.findUserSmsDltPriceByUserId(UserIdParent);
                     BigDecimal SmsDltPriceParent = convertStringToBigDecimal(smsDltPricParent);

                    // BigDecimal smsDltPriceParentBigDecimal = new BigDecimal(SmsDltPriceParent);
                     System.out.println("SmsDltPriceParent:- "+SmsDltPriceParent);
               
              
                         BigDecimal totalPriceParent = priceParent.add(SmsDltPriceParent);
                         finalPriceParent = totalPriceParent.multiply(BigDecimal.valueOf(100));
                         
                         System.out.println("Final Price of parent Id(sum of price and SmsDltPrice, multiplied by 100):- " + finalPriceParent);
                }
                }  
              
                BigDecimal price;
                //protrans price
                if(protrans.equals("trans")){
                	  price = userRepository.findTransUnitPriceByUserId(userId);
                     System.out.println("price:- "+price);
                }
                else {
                	 price = userRepository.findPromoUnitPriceByUserId(userId);
                    System.out.println("price:- "+price);
                }
                
                //
                String SmsDltPric = userRepository.findUserSmsDltPriceByUserId(userId);
                BigDecimal SmsDltPrice = convertStringToBigDecimal(SmsDltPric);
              //  BigDecimal smsDltPriceBigDecimal = new BigDecimal(SmsDltPrice);
                System.out.println("SmsDltPrice:- "+ SmsDltPrice);
               
         
                    BigDecimal totalPrice = price.add(SmsDltPrice);
                    BigDecimal finalPrice = totalPrice.multiply(BigDecimal.valueOf(100));
                    
                    System.out.println("Final Price (sum of price and SmsDltPrice, multiplied by 100):- " + finalPrice);
   
                if (userCode != null) {
                    boolean flag = isMultiSendPositionCorrect(api);
                    if (flag) {
                        BigInteger amountBefore = userAccountRepository.findAmountByUserCode(userCode);
                        System.out.println("Amount before: " + amountBefore);
                        
                         
                        if (!"superreseller".equals(groupPermission)) {                       
                        	amountBeforeParent = userAccountRepository.findAmountByUserCode(parentUserCode);
                        System.out.println("Amount before of parent:- " + amountBeforeParent);
                        }
                       
                        

                        URI url = UriComponentsBuilder.fromHttpUrl(api).build().toUri();
                        System.out.println(url);
                        HttpHeaders header = new HttpHeaders();
                        HttpEntity<String> entity = new HttpEntity<>(header);

                        System.out.println("multisend");
                        ResponseEntity<SubmitResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, SubmitResponse.class);
                        SubmitResponse submitResponse = response.getBody();
                        System.out.println(submitResponse);

                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        for (UserReport userReport : submitResponse.getSubmitResponses()) {
                            System.out.println(userReport.toString());
                            int pdu = userReport.getPdu();
                            String state = userReport.getState();
                            Long transactionId = userReport.getTransactionId();
                            String description = userReport.getDescription();

                            Integer reportUserCode = userMdrRepository.findUserCodeByTransactionId(transactionId);
                            System.out.println(reportUserCode);
                            BigInteger amountAfter = userAccountRepository.findAmountByUserCode(userCode);
                            System.out.println("Amount after: " + amountAfter);

                            BigInteger totalDeduction = amountBefore.subtract(amountAfter);
                            System.out.println("Total deduction of user:- " + totalDeduction);
                            
                            
                            
                            Integer total = countTotalRecipients(url);
                            BigInteger finalPriceBigInt = finalPrice.toBigInteger();
                            boolean status = totalDeduction.equals((finalPriceBigInt).multiply(BigInteger.valueOf(pdu)).multiply(BigInteger.valueOf(total)));
                            
                            if (!"superreseller".equals(groupPermission)) {                              
                            	 amountAfterParent = userAccountRepository.findAmountByUserCode(parentUserCode);
                                System.out.println("Amount After of parent:- " + amountAfterParent);
                                
                                 totalDeductionParent = amountBeforeParent.subtract(amountAfterParent);
                                System.out.println("Total deduction of parent user:- " + totalDeductionParent);
                                
                                BigInteger finalPriceParentBigInt= finalPriceParent.toBigInteger();

                                 statusParent = totalDeductionParent.equals((finalPriceParentBigInt).multiply(BigInteger.valueOf(pdu)).multiply(BigInteger.valueOf(total)));
                                }
                            reports.add(new ApiReport(api, amountBefore, amountAfter, totalDeduction, status, description, state, pdu,transactionId, groupPermission, userCode, parentUserCode,amountBeforeParent, amountAfterParent,totalDeductionParent, statusParent ));
                        }
                    } else {
                        userCode = userRepository.findUserCodeByUserId(userId);
                        BigInteger amountBefore = userAccountRepository.findAmountByUserCode(userCode);
                        System.out.println("Amount before: " + amountBefore);
                        
                         amountBeforeParent = null;
                        if (!"superreseller".equals(groupPermission)) {        
                        	amountBeforeParent = userAccountRepository.findAmountByUserCode(parentUserCode);
                        System.out.println("Amount before of parent:- " + amountBeforeParent);
                        }
                        
                        URI url = UriComponentsBuilder.fromHttpUrl(api).build().toUri();
                        System.out.println(url);
                        HttpHeaders header = new HttpHeaders();
                        HttpEntity<String> entity = new HttpEntity<>(header);

                        ResponseEntity<UserReport> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserReport.class);

                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        UserReport userReport = response.getBody();
                        System.out.println(userReport.toString());
                        int pdu = userReport.getPdu();
                        String state = userReport.getState();
                        Long transactionId = userReport.getTransactionId();
                        String description = userMdrRepository.findDescriptionByTransactionId(transactionId);
                        if ("SUBMIT_FAILED".equals(state)) {
                            description = userReport.getDescription();
                        }
                        BigInteger amountAfter = userAccountRepository.findAmountByUserCode(userCode);
                        System.out.println("Amount after: " + amountAfter);
                        BigInteger totalDeduction = amountBefore.subtract(amountAfter);

                        boolean status = true;
                        String messageState = userMdrRepository.findmessageStateByTransactionId(transactionId);
                        if ("PSB_GENERIC_ERROR".equals(messageState)) {
                            if (totalDeduction.equals(BigInteger.ZERO)) {
                                status = true;
                            } else {
                                status = false;
                            }
                        } else {
                        	
                        	  BigInteger finalPriceBigInt = finalPrice.toBigInteger();
                               status = totalDeduction.equals((finalPriceBigInt).multiply(BigInteger.valueOf(pdu)));
                        }
                        
                        if (!"superreseller".equals(groupPermission)) {                         
                        	 amountAfterParent = userAccountRepository.findAmountByUserCode(parentUserCode);
                            System.out.println("Amount before of parent:- " + amountAfterParent);
                            
                             totalDeductionParent = amountBeforeParent.subtract(amountAfterParent);
                            System.out.println("Total deduction of parent user:- " + totalDeductionParent);
                            
                            BigInteger finalPriceParentBigInt= finalPriceParent.toBigInteger();
                             statusParent = totalDeductionParent.equals((finalPriceParentBigInt).multiply(BigInteger.valueOf(pdu)));
                            }
                        reports.add(new ApiReport(api, amountBefore, amountAfter, totalDeduction, status, description, state, pdu,transactionId, groupPermission, userCode, parentUserCode,amountBeforeParent, amountAfterParent,totalDeductionParent, statusParent ));
                    }
                }
            }
        }
        generateExcelReport(reports);
    }

   

	private List<String> readApiFile(String filePath) throws IOException {
        List<String> apis = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                apis.add(line);
            }
        }
        return apis;
    }

	 private String extractUserId(String apiUrl) {
	        try {
	            URL url = new URL(apiUrl);
	            String query = url.getQuery();
	            if (query != null) {
	                String[] pairs = query.split("&");
	                for (String pair : pairs) {
	                    int idx = pair.indexOf("=");
	                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
	                    String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
	                    if (key.equals("username")) {
	                        String[] parts = value.split("\\.");
	                            return parts[0]; // Return userId
	                        }
	                    }
	                }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 
	 
	 public String extractProtrans(String apiUrl) {
	        try {
	            URL url = new URL(apiUrl);
	            String query = url.getQuery();
	            if (query != null) {
	                String[] pairs = query.split("&");
	                for (String pair : pairs) {
	                    int idx = pair.indexOf("=");
	                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
	                    String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
	                    if (key.equals("username")) {
	                        String[] parts = value.split("\\.");
	                        if (parts.length == 2) {
	                            return parts[1]; // Return protrans
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
    private static boolean isMultiSendPositionCorrect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            String path = url.getPath();
            String query = url.getQuery();

            if (path.contains("/multiSend") && query != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int countTotalRecipients(URI uri) {
        String query = uri.getQuery();
        String[] params = query.split("&");

        for (String param : params) {
            if (param.startsWith("to=")) {
                String numbers = param.substring(3);
                String[] individualNumbers = numbers.split(",");
                return individualNumbers.length;
            }
        }

        return 0;
    }

    private BigDecimal convertStringToBigDecimal(String value) {
        if (value == null) {
            return BigDecimal.ZERO; // or handle appropriately
        }
        try {
            // Remove any quotes or whitespace
            String cleanedValue = value.replace("\"", "").trim();
            return new BigDecimal(cleanedValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid number format: " + value, e);
        }
    }
    
    
    private void generateExcelReport(List<ApiReport> reports) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("API Report");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"API URL", "Amount Before", "Amount After", "Total Deduction", "Status", "Description", "Pdu", "State", "messageId", "GroupPermission", "StatusCodeUser", "ParentUserCode","BeforeAmountParent","AfterAmountParent", "ParentDeduction","ParentStatus"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (ApiReport report : reports) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(report.getApiUrl());
            row.createCell(1).setCellValue(report.getAmountBefore().toString());
            row.createCell(2).setCellValue(report.getAmountAfter().toString());
            row.createCell(3).setCellValue(report.getTotalDeduction().toString());
            row.createCell(4).setCellValue(report.isStatus() ? "True" : "False");
            row.createCell(5).setCellValue(report.getDescription());
            row.createCell(6).setCellValue(String.valueOf(report.getPdu()));
            row.createCell(7).setCellValue(report.getState());
            row.createCell(8).setCellValue(report.getTransactionId().toString());
            row.createCell(9).setCellValue(report.getGroupPermission());
            row.createCell(10).setCellValue(report.getUserCode() != null ? report.getUserCode().toString() : "N/A");
            row.createCell(11).setCellValue(report.getParentUserCode() != null ? report.getParentUserCode().toString() : "N/A");
            row.createCell(12).setCellValue(report.getAmountBeforeParent() != null ? report.getAmountBeforeParent().toString() : "N/A");
            row.createCell(13).setCellValue(report.getAmountAfterParent() != null ? report.getAmountAfterParent().toString() : "N/A");
            row.createCell(14).setCellValue(report.getTotalDeductionParent() != null ? report.getTotalDeductionParent().toString() : "N/A");
            row.createCell(15).setCellValue(report.isStatusParent() ? "True" : "False");
        }

        try (FileOutputStream fileOut = new FileOutputStream("C:\\springg\\ApiReport.xlsx")) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    private static class ApiReport {
    private String apiUrl;
    private BigInteger amountBefore;
    private BigInteger amountAfter;
    private BigInteger totalDeduction;
    private boolean status;
    private String description;
    private String state;
    private int pdu;  
    private Long transactionId;
   	private String groupPermission;
    private Integer userCode;
    private Integer parentUserCode;
    private BigInteger amountBeforeParent;
    private BigInteger amountAfterParent;
    private BigInteger totalDeductionParent;
    private boolean statusParent;
    
    public ApiReport(String apiUrl, BigInteger amountBefore, BigInteger amountAfter, BigInteger totalDeduction,
            boolean status, String description, String state, int pdu, Long transactionId, String groupPermission,
            Integer userCode, Integer parentUserCode, BigInteger amountBeforeParent, BigInteger amountAfterParent,
            BigInteger totalDeductionParent, boolean statusParent) {
this.apiUrl = apiUrl;
this.amountBefore = amountBefore;
this.amountAfter = amountAfter;
this.totalDeduction = totalDeduction;
this.status = status;
this.description = description;
this.state = state;
this.pdu = pdu;
this.transactionId = transactionId;
this.groupPermission = groupPermission;
this.userCode = userCode;
this.parentUserCode = parentUserCode;
this.amountBeforeParent = amountBeforeParent;
this.amountAfterParent = amountAfterParent;
this.totalDeductionParent = totalDeductionParent;
this.statusParent = statusParent;
}


	public String getGroupPermission() {
		return groupPermission;
	}

	public Integer getUserCode() {
		return userCode;
	}

	public Integer getParentUserCode() {
		return parentUserCode;
	}

	public BigInteger getAmountBeforeParent() {
		return amountBeforeParent;
	}

	public BigInteger getAmountAfterParent() {
		return amountAfterParent;
	}

	public BigInteger getTotalDeductionParent() {
		return totalDeductionParent;
	}

	public boolean isStatusParent() {
		return statusParent;
	}
    
        public String getApiUrl() {
            return apiUrl;
        }

        public BigInteger getAmountBefore() {
            return amountBefore;
        }

        public BigInteger getAmountAfter() {
            return amountAfter;
        }

        public BigInteger getTotalDeduction() {
            return totalDeduction;
        }

        public boolean isStatus() {
            return status;
        }

        public String getDescription() {
            return description;
        }

        public String getState() {
            return state;
        }

        public int getPdu() {
            return pdu;
        }
        public Long getTransactionId() {
    		return transactionId;
    	}
}
}
