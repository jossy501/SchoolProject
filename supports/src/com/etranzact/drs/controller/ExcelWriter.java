/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.etranzact.drs.controller;

import com.etranzact.drs.dto.Claim;
import com.etranzact.drs.utility.DateUtility;
import com.etranzact.supportmanager.action.ReportAction;
import com.etranzact.supportmanager.dto.E_SETTLEMENTDOWNLOAD_BK;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author akachukwu.ntukokwu
 */
public class ExcelWriter {


    public java.io.File write(List<Claim> rec)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String c = sc.getRealPath("upload");
            int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
            if (tmpIndex > 0) {
                c = c.substring(0, tmpIndex) + java.io.File.separator + "DRS Report Downloads";
            }
            java.io.File f = new java.io.File(c);
            if (!f.exists()) {
                f.mkdirs();
            }
        java.io.File excelFile = new java.io.File (f.toString() + "\\DRS Report_" + new DateUtility().dateTimeStamp() + ".xls");

        try{
        
           WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
            WritableSheet sheet = workbook.createSheet("DRS Report " + new DateUtility().dateTimeStamp(), 0);

            WritableFont times16font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, true);
            WritableCellFormat times16format = new WritableCellFormat(times16font);

            Label calimID = new Label(0, 3, "Claim ID", times16format);
            sheet.addCell(calimID);
            Label IssuingBank = new Label(1, 3, "Issuing Bank", times16format);
            sheet.addCell(IssuingBank);
            Label sendingBank = new Label(2, 3, "Sending Bank", times16format);
            sheet.addCell(sendingBank);
            Label channelType = new Label(3, 3, "Channel Type", times16format);
            sheet.addCell(channelType);
            Label dispute = new Label(4, 3, "Dispute", times16format);
            sheet.addCell(dispute);
            Label cardsNumber = new Label(5, 3, "Cards Number", times16format);
            sheet.addCell(cardsNumber);
            Label amount = new Label(6, 3, "Amount", times16format);
            sheet.addCell(amount);
            Label dateLogged = new Label(7, 3, "Date Logged", times16format);
            sheet.addCell(dateLogged);
            Label dueDate = new Label(8, 3, "Due Date", times16format);
            sheet.addCell(dueDate);
            Label status = new Label(9, 3, "Status", times16format);
            sheet.addCell(status);
            Label STAN = new Label(10, 3, "Global ID", times16format);
            sheet.addCell(STAN);
            Label SysID = new Label(11, 3, "SysID", times16format);
            sheet.addCell(SysID);
            Label clusedUser = new Label(12, 3, "Accept/Decline", times16format);
            sheet.addCell(clusedUser);
            String inVlaue = "";
            for (int w = 0; w < rec.size(); w++) {
                Claim claim = rec.get(w);
                    calimID = new Label(0, 4 + w, claim.getSysID() + "");
                    sheet.addCell(calimID);
                    try{IssuingBank = new Label(1, 4 + w, claim.getClBank().getBankname());}catch(Exception d){}
                    sheet.addCell(IssuingBank);
                    sendingBank = new Label(2, 4 + w, claim.getDecodeSentBank());
                    sheet.addCell(sendingBank);
                    channelType = new Label(3, 4 + w, claim.getChannel().getChannelName());
                    sheet.addCell(channelType);
                    dispute = new Label(4, 4 + w, claim.getClDispute().getDispute());
                    sheet.addCell(dispute);
                    cardsNumber = new Label(5, 4 + w, claim.getCardNo());
                    sheet.addCell(cardsNumber);
                    amount = new Label(6, 4 + w, claim.getFormatAmount());
                    sheet.addCell(amount);
                    dateLogged = new Label(7, 4 + w, claim.getCreatedDate() + "");
                    sheet.addCell(dateLogged);
                    dueDate = new Label(8, 4 + w, claim.getDueDate() + "");
                    sheet.addCell(dueDate);
                    status = new Label(9, 4 + w, claim.getStatusDescription());
                    sheet.addCell(status);
                    STAN = new Label(10, 4 + w, claim.getTransactionStan());
                    sheet.addCell(STAN);
                    SysID = new Label(11, 4 + w, claim.getSysID());
                    sheet.addCell(SysID);
                    clusedUser = new Label(12, 4 + w, claim.getDecodeClosedUser());
                    sheet.addCell(clusedUser);
                    inVlaue = inVlaue + "," + claim.getId() + "";
                    
                


            }
            workbook.write();
            workbook.close();
            
        }catch(Exception d)
        {
            d.printStackTrace();
        }


        return excelFile;
    }

    public java.io.File write2(List rec)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String c = sc.getRealPath("upload");
            int tmpIndex = c.lastIndexOf(java.io.File.separator + "tmp" + java.io.File.separator);
            if (tmpIndex > 0) {
                c = c.substring(0, tmpIndex) + java.io.File.separator + "Support GTBReport Downloads";
            }
            java.io.File f = new java.io.File(c);
            if (!f.exists()) {
                f.mkdirs();
            }
        java.io.File excelFile = new java.io.File (f.toString() + "\\Report_" + new DateUtility().dateTimeStamp() + ".xls");

        try{

           WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
            WritableSheet sheet = workbook.createSheet("Report " + new DateUtility().dateTimeStamp(), 0);

            WritableFont times16font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, true);
            WritableCellFormat times16format = new WritableCellFormat(times16font);

            Label calimID = new Label(0, 3, "S/N", times16format);
            sheet.addCell(calimID);
            Label IssuingBank = new Label(1, 3, "Trans Date", times16format);
            sheet.addCell(IssuingBank);
            Label sendingBank = new Label(2, 3, "Merchant Code", times16format);
            sheet.addCell(sendingBank);
            Label channelType = new Label(3, 3, "Transaction No", times16format);
            sheet.addCell(channelType);
            Label dispute = new Label(4, 3, "Transaction Description", times16format);
            sheet.addCell(dispute);
            Label cardsNumber = new Label(5, 3, "Reference", times16format);
            sheet.addCell(cardsNumber);
            Label amount = new Label(6, 3, "Transaction Amount", times16format);
            sheet.addCell(amount);
            String inVlaue = "";
            for (int w = 0; w < rec.size(); w++) {
                E_SETTLEMENTDOWNLOAD_BK tran = (E_SETTLEMENTDOWNLOAD_BK)rec.get(w);
                    calimID = new Label(0, 4 + w, w+1+ "");
                    sheet.addCell(calimID);
                    try{IssuingBank = new Label(1, 4 + w, tran.getTrans_date());}catch(Exception d){}
                    sheet.addCell(IssuingBank);
                    sendingBank = new Label(2, 4 + w,tran.getMerchat_code());
                    sheet.addCell(sendingBank);
                    channelType = new Label(3, 4 + w,tran.getTrans_no());
                    sheet.addCell(channelType);
                    dispute = new Label(4, 4 + w,tran.getTrans_desc());
                    sheet.addCell(dispute);
                    cardsNumber = new Label(5, 4 + w,tran.getChannelid());
                    sheet.addCell(cardsNumber);
                    jxl.write.Number amount_ = new jxl.write.Number(6, 4 + w,(new ReportAction().makeDouble(tran.getTrans_amount())));
                    sheet.addCell(amount_);

            }
            workbook.write();
            workbook.close();

        }catch(Exception d)
        {
            d.printStackTrace();
        }


        return excelFile;
    }

}
