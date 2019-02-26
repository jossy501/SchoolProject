package com.etranzact.excel;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
015
* @author giftsam
016
*/
public class ExcelSheetReader{

/**
021
* This method is used to read the data's from an excel file.
022
* @param fileName - Name of the excel file.
023
*/
private void readExcelFile(String fileName)
{

/**
027
* Create a new instance for cellDataList
028
*/

List cellDataList = new ArrayList();

try

{

	FileInputStream fileInputStream = new FileInputStream(fileName);

 
	POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);

	HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
	HSSFSheet hssfSheet = workBook.getSheetAt(0); 

	Iterator rowIterator = hssfSheet.rowIterator();

		while (rowIterator.hasNext())
		{
			HSSFRow hssfRow = (HSSFRow) rowIterator.next();

			Iterator iterator = hssfRow.cellIterator();

			List cellTempList = new ArrayList();

			while (iterator.hasNext())
			{
				HSSFCell hssfCell = (HSSFCell) iterator.next();

				cellTempList.add(hssfCell);

			}

			cellDataList.add(cellTempList);

		}

	}
	catch (Exception e)
	{

		e.printStackTrace();

	}

		printToConsole(cellDataList);

	}

	private void printToConsole(List cellDataList)
	{

			for (int i = 0; i < cellDataList.size(); i++){

				List cellTempList = (List) cellDataList.get(i);

				for (int j = 0; j < cellTempList.size(); j++)

				{

					HSSFCell hssfCell = (HSSFCell) cellTempList.get(j);
					String stringCellValue = hssfCell.toString();

					System.out.print(stringCellValue + "\t");
				}

					System.out.println();
	}	

}

/*public static void main(String[] args)
{

		String fileName = "C:" + File.separator + "Users" +

		File.separator + "Giftsam" + File.separator + "Desktop" +

		File.separator + "sampleexcel.xls";

		new ExcelSheetReader().readExcelFile(fileName);

	}
*/
}

