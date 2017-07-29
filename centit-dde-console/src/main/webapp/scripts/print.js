		var tableToExcel = (function() {
			  var uri = 'data:application/vnd.ms-excel;base64,'
			    , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'
			    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
			    , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
			    
				var explorer = window.navigator.userAgent ;
				if (explorer.indexOf("MSIE") >= 0) {
					return function(table,name){
						var curTbl = document.getElementById(table);
					     var oXL ;
					     
					     
					     try {
					    	 oXL = new ActiveXObject('Excel.Application');
					     }
					     catch (e) {
					         alert("无法启动Excel!\n\n" + e.message +
					            "\n\n如果您确信您的电脑中已经安装了Excel，"+
					            "那么请调整IE的安全级别(安全级 - 低)。\n\n具体操作：\n\n"+
					            "工具 → Internet选项 → 安全 → 拉动安全级至(低)");
					         return false;
					     }
					     //创建AX对象excel
					     var oWB = oXL.Workbooks.Add();
					     //获取workbook对象
					    var oSheet = oWB.ActiveSheet;
					    //激活当前sheet
					     var Lenr = curTbl.rows.length;
					     //取得表格行数
					     var myWorksheet=oSheet; 
					     var readRow =0, readCol = 0, myCellColSpan =0,myCellRowSpan=0, totalCol=0; 
					   //开始构件模拟表格
					     var excelTable = new Array();					   
					   
				            for (i = 0; i < curTbl.rows[0].cells.length; i++) {
				                myHTMLTableCell = curTbl.rows(0).cells(i);
				                myCellColSpan = myHTMLTableCell.colSpan;
				                totalCol = totalCol + myCellColSpan;    
				            }
				            totalRow = curTbl.rows.length;  
					     for (i = 0; i <= totalRow; i++) {
					         excelTable[i] = new Array();
					         for (t = 0; t <= totalCol; t++) {
					             excelTable[i][t] = false;
					         }
					     }
					     
					     for (i = 0; i < Lenr; i++)
					     {
					         var Lenc = curTbl.rows(i).cells.length;
					         //取得每行的列数
					         readRow = i + 1;
					         readCol = 1;
					         
					         for (j = 0; j < Lenc; j++)
					         {
					             myHTMLTableCell = curTbl.rows(i).cells(j);
					             myCellColSpan = myHTMLTableCell.colSpan;
					             myCellRowSpan = myHTMLTableCell.rowSpan;
					             for (y = 1; y <= totalCol; y++) {
					                 if (excelTable[readRow][y] == false) {
					                     readCol = y;
					                     break;
					                 }
					             }
					             if (myCellColSpan * myCellRowSpan > 1) {
					                 myExcelCell = myWorksheet.Cells(readRow, readCol);
					                 myExcelCell2 = myWorksheet.Cells(readRow + myCellRowSpan - 1, readCol + myCellColSpan - 1);
					                 myWorksheet.Range(myExcelCell, myExcelCell2).Merge();
					                 myExcelCell.Value = myHTMLTableCell.innerText;
					                 //myExcelCell.columnWidth = -1;
					                 //myExcelCell.lineWrap  = true;
					                 for (row = readRow; row <= myCellRowSpan + readRow - 1; row++) {
					                     for (col = readCol; col <= myCellColSpan + readCol - 1; col++) {
					                         excelTable[row][col] = true;
					                     }
					                 }
					                 readCol = readCol + myCellColSpan;
					                  
					             }else{					            	 
					                 myExcelCell = myWorksheet.Cells(readRow, readCol);
					                 //myExcelCell.columnWidth = -1;
					                 //myExcelCell.lineWrap  = true;
					                 myExcelCell.Value = myHTMLTableCell.innerText;
					                 excelTable[readRow][readCol] = true;
					                 readCol = readCol + 1; 
					             }    
					            // oSheet.Cells(i + 1, j + 1).value = curTbl.rows(i).cells(j).innerText;
					             //赋值
					         }
					     }
					     oXL.Visible = true;
					     
					  
					};
					
				}else{  
			    
			  return function(table, name) {
			    if (!table.nodeType) table = document.getElementById(table)
			    var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
			    window.location.href = uri + base64(format(template, ctx))
			  };
			}
			})();
function doExport(){
  bdhtml=window.document.body.innerHTML
  sprnstr="<!--startprint-->"
  eprnstr="<!--endprint-->"
  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+18)
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
  ExcelSheet = new ActiveXObject('Excel.Application');
  var wlexport=window.open("","wlexport","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
  wlexport.document.write("<link rel=stylesheet type=text/css href=print.css>"+prnhtml)
  wlexport.document.close()
  ExcelSheet.Application.Visible = true;
  var mywork=ExcelSheet.workbooks.add;
  var mydoc=mywork.sheets.add(mywork.worksheets(1));
  var sel=wlexport.document.body.createTextRange();
  //var myData= new GetTableData(bdhtml,0,0);
  sel.select();
  wlexport.document.execCommand('Copy');
  sel.moveEnd('character');
  wlexport.close();
  mydoc.paste();
  mydoc.usedrange.Hyperlinks.DELETE();
  mydoc.Rows('1:1').Font.Name = "\u5B8B\u4F53";  
  mydoc.Rows('1:1').Font.Color = (15,16,27); 
  //mydoc.Rows('1:1').Font.Size = 22;  
  //mydoc.Rows('1:1').Font.Bold = true; 
  mydoc.Rows('1:1').HorizontalAlignment = 3;
  mydoc.Rows(''+mydoc.UsedRange.Rows.Count+':2').Font.Size = 12;  
  mydoc.Rows(''+mydoc.UsedRange.Rows.Count+':2').HorizontalAlignment = 4;
  mydoc.Columns('A:A').HorizontalAlignment = 2;
  //mydoc.Rows('4:'+(4+mydoc.rows-1)).Font.Size = 12;  
  mydoc.usedrange.borders.linestyle=1;
  mydoc.usedrange.borders.weight=2;
  mydoc.usedrange.borders.colorindex=-4105;
  mydoc.usedrange.WrapText=false;
  doExportFormat(mydoc.columns, mydoc.rows);
  mydoc.usedrange.columns.AutoFit();
  window.document.body.innerHTML=prnhtml; 
  window.print(); 
	
//	window.clipboardData.setData("Text",document.all('list stat').outerHTML);
//	try
//	{
//	var ExApp = new ActiveXObject("Excel.Application")
//	var ExWBk = ExApp.workbooks.add()
//	var ExWSh = ExWBk.worksheets(1)
//	ExApp.DisplayAlerts = false
//	ExApp.visible = true
//	}  
//	catch(e)
//	{
//	alert("您的电脑没有安装Microsoft Excel软件！")
//	return false
//	} 
//	 ExWBk.worksheets(1).Paste;	

}
function doExport_fresh(){
  bdhtml=window.document.body.innerHTML
  sprnstr="<!--startprint-->"
  eprnstr="<!--endprint-->"
  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+18)
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
  ExcelSheet = new ActiveXObject('Excel.Application');
  var wlexport=window.open("","wlexport","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
  wlexport.document.write("<link rel=stylesheet type=text/css href=./css/amprint.css>"+prnhtml)
  wlexport.document.close()
  ExcelSheet.Application.Visible = true;
  var mywork=ExcelSheet.workbooks.add;
  var mydoc=mywork.sheets.add(mywork.worksheets(1));
  var sel=wlexport.document.body.createTextRange();
  //var myData= new GetTableData(bdhtml,0,0);
  sel.select();
  wlexport.document.execCommand('Copy');
  sel.moveEnd('character');
  wlexport.close();
  mydoc.paste();
  mydoc.usedrange.Hyperlinks.DELETE();
  mydoc.Rows('1:1').Font.Name = "\u5B8B\u4F53";  
  mydoc.Rows('1:1').Font.Color = (15,16,27); 
  mydoc.Rows('1:1').Font.Size = 22;  
  mydoc.Rows('1:1').Font.Bold = true; 
  mydoc.Rows('1:1').HorizontalAlignment = 3;   
  mydoc.Rows('2:2').Font.Size = 12;
  mydoc.Rows(2).RowHeight =60;  
  mydoc.Rows(''+mydoc.UsedRange.Rows.Count+':3').Font.Size = 11;  
  mydoc.Rows(''+mydoc.UsedRange.Rows.Count+':3').HorizontalAlignment = 4;  
  mydoc.Columns('A:A').HorizontalAlignment = 5;  
  mydoc.Columns('B:B').HorizontalAlignment = 4;
  //mydoc.Rows('4:'+(4+mydoc.rows-1)).Font.Size = 12;  
  mydoc.usedrange.borders.linestyle=1;
  mydoc.usedrange.borders.weight=2;
  mydoc.usedrange.borders.colorindex=-4105;
  mydoc.usedrange.WrapText=true;
 //doExportFormat(mydoc.columns, mydoc.rows);
  mydoc.usedrange.columns.AutoFit();
}
function doExportSimple(){
  bdhtml=window.document.body.innerHTML
  sprnstr="<!--startprint-->"
  eprnstr="<!--endprint-->"
  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17)
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
  ExcelSheet = new ActiveXObject('Excel.Application');
  var wlexport=window.open("","wlexport","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
  wlexport.document.write("<link rel=stylesheet type=text/css href=./css/amprint.css>"+prnhtml)
  wlexport.document.close()
  ExcelSheet.Application.Visible = true;
  var mywork=ExcelSheet.workbooks.add;
   var oSheet = mywork.ActiveSheet; 

  var mydoc=mywork.sheets.add(mywork.worksheets(1));
  var sel=wlexport.document.body.createTextRange();
  //var myData= new GetTableData(bdhtml,0,0);
  sel.select();
  
  wlexport.document.execCommand('Copy');
  sel.moveEnd('character');   
  wlexport.close();
  mydoc.paste();
  
  
  mydoc.usedrange.borders.linestyle=1;
  ExcelSheet.Selection.HorizontalAlignment = -4108;       
  ExcelSheet.Range("a1:b1:c1:d1:f1:g1:h1:i1:j1:k1").MergeCells = true;
  ExcelSheet.Range("a1:b1").select;
  ExcelSheet.Selection.VerticalAlignment = -4108; 
  mydoc.Columns.AutoFit; 
  mydoc.Columns('D:BM').ColumnWidth  = 2;  
  mydoc.Rows.AutoFit;
}


function doExportZf(){
  bdhtml=window.document.body.innerHTML
  sprnstr="<!--startprint-->"
  eprnstr="<!--endprint-->"
  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17)
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
  ExcelSheet = new ActiveXObject('Excel.Application');
  var wlexport=window.open("","wlexport","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
  wlexport.document.write("<link rel=stylesheet type=text/css href=./css/amprint.css>"+prnhtml)
  wlexport.document.close()
  ExcelSheet.Application.Visible = true;
  var mywork=ExcelSheet.workbooks.add;
   var oSheet = mywork.ActiveSheet; 

  var mydoc=mywork.sheets.add(mywork.worksheets(1));
  var sel=wlexport.document.body.createTextRange();
  //var myData= new GetTableData(bdhtml,0,0);
  sel.select();
  
  wlexport.document.execCommand('Copy');
  sel.moveEnd('character');   
  wlexport.close();
  mydoc.paste();
  
  
  mydoc.usedrange.borders.linestyle=1;
  ExcelSheet.Selection.HorizontalAlignment = -4108;       
  ExcelSheet.Range("a1:b1:c1").MergeCells = true;
  ExcelSheet.Range("a1:b1").select;
  ExcelSheet.Selection.VerticalAlignment = -4108; 
  mydoc.Columns.AutoFit; 
  mydoc.Columns('D:BM').ColumnWidth  = 2; 
  mydoc.Rows.AutoFit;
  mydoc.Rows(4).RowHeight =120;
}





function doExportList(){
  bdhtml=window.document.body.innerHTML
  sprnstr="<!--startprint-->"
  eprnstr="<!--endprint-->"
  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17)
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
  ExcelSheet = new ActiveXObject('Excel.Application');
  var wlexport=window.open("","wlexport","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
  wlexport.document.write("<link rel=stylesheet type=text/css href=./css/amprint.css>"+prnhtml)
  wlexport.document.close()
  ExcelSheet.Application.Visible = true;
  var mywork=ExcelSheet.workbooks.add;
  var mydoc=mywork.sheets.add(mywork.worksheets(1));
  var sel=wlexport.document.body.createTextRange();
  //var myData= new GetTableData(bdhtml,0,0);
  sel.select();
  wlexport.document.execCommand('Copy');
  sel.moveEnd('character');
  wlexport.close();
  mydoc.paste();
  mydoc.usedrange.Hyperlinks.DELETE();

  mydoc.Rows(''+mydoc.UsedRange.Rows.Count+':1').Font.Size= 12;  
  //mydoc.Rows('4:'+(4+mydoc.rows-1)).Font.Size = 12;  
  mydoc.usedrange.borders.linestyle=1;
  mydoc.usedrange.borders.weight=2;
  mydoc.usedrange.borders.colorindex=-4105;
  mydoc.usedrange.WrapText=false;
  //doExportFormat(mydoc.columns, mydoc.rows);
  mydoc.usedrange.columns.AutoFit();
  //mydoc.Rows(''+mydoc.UsedRange.Rows.Count+':1').ColumnWidth = 20;  
    var i;
    var j=mydoc.UsedRange.Columns.Count;
    var str="";
    for(i = 1; i <= j; i=i+1){
    	if(mydoc.UsedRange.Columns(i).ColumnWidth > 20 ){
	    //alert(mydoc.UsedRange.Columns(i).ColumnWidth);
	    	str=''+i.toString()+':'+i.toString()+'';
	    	//alert(str);  	
	  		mydoc.Columns(str).ColumnWidth = 20;     
		}
	}
    mydoc.usedrange.WrapText = true;
    mydoc.usedrange.Rows.AutoFit();
}

/*
 * 把前台table中数据提交到后台共POI导出excel
 * 		前端form id="dataForm" action="twodimenform!doExport.do?headrows=1" headrows:表头行数，可选
 * 			table id="dataTab"
 * 		需添加 <input name="exportData" id="exportData" type="hidden" ></input> （导出数据）
 * 			  <input name="formName" id="formName" value="${formName }" type="hidden" ></input>（文件名，可选）	
 * */
function exportTableToExcel() {
	var doc = document.getElementById("dataTab");
	var rows = doc.rows.length; //表格的总行数 
	var dataInfo = ""; //传入后台的数据 
	var maxcol = 0; //最大的列数 

	for ( var r = 0; r < rows; r++) {
		var colspan = 1;//单元格所占单元格数 
		var rowspan = 1;//单元格所跨的行数 
		var current = 1;//td当前打印位置 
		var maxcolumn = doc.rows[r].cells.length;//当前行列数
		for (c = 0; c < maxcolumn; c++) {
			if (maxcol < maxcolumn) {
				maxcol = maxcolumn;
			}
		}

		var temp = 0;
		for ( var i = 0; i < maxcol; i++) {
			//var rowscount=0;//td当前所跨的行数 
			if (doc.rows[r].cells[i] != null) {
				colspan = doc.rows[r].cells[i].colSpan; //取的当前单元格所占单元格列数 
				rowspan = doc.rows[r].cells[i].rowSpan; //取得当前单元格所占单元格行数 
				dataInfo = dataInfo + r + "," + i + "," + rowspan + ","
						+ colspan + "," + doc.rows[r].cells[i].innerText + "#";
			}
		}
	}
	document.getElementById("exportData").value = dataInfo;
	document.getElementById("dataForm").submit();
}

function doPrint(){
//  bdhtml=window.document.body.innerHTML
//  sprnstr="<!--startprint-->"
//  eprnstr="<!--endprint-->"
//  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17)
//  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
//  wlprint=window.open("","wlprint","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
//  wlprint.document.write("<link rel=stylesheet type=text/css href=./css/amprint.css>"+prnhtml)
//  wlprint.document.close();
//  wlprint.print();
//  wlprint.close();
  
  bdhtml=window.document.body.innerHTML;//获取当前页的html代码 
  sprnstr="<!--startprint-->";
  eprnstr="<!--endprint-->";//设置打印结束区域 
  prnhtml=bdhtml.substring(bdhtml.indexOf(sprnstr)+18); //从开始代码向后取html 
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html 
  window.document.body.innerHTML=prnhtml; 
  window.print(); 
  window.document.body.innerHTML=bdhtml; 

}

function doPrintStat(){
  bdhtml=window.document.body.innerHTML
  sprnstr="<!--startprint-->"
  eprnstr="<!--endprint-->"
  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17)
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
  wlprint=window.open("","wlprint","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
  wlprint.document.write("<link rel=stylesheet type=text/css href=./css/amprint.css>"+prnhtml)
  wlprint.document.close();
  wlprint.print();
  wlprint.close();
  
//  bdhtml=window.document.body.innerHTML;//获取当前页的html代码 
//  sprnstr="<!--startprint-->";
//  eprnstr="<!--endprint-->";//设置打印结束区域 
//  prnhtml=bdhtml.substring(bdhtml.indexOf(sprnstr)+18); //从开始代码向后取html 
//  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html 
//  window.document.body.innerHTML=prnhtml; 
//  window.print(); 
//  window.document.body.innerHTML=bdhtml; 

}

function doExportFormat(columns, rows){
  bdhtml=window.document.body.innerHTML
  sprnstr="<!--startprint-->"
  eprnstr="<!--endprint-->"
  prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17)
  prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr))
  ExcelSheet = new ActiveXObject('Excel.Application');
  var wlexport=window.open("","wlexport","left=0,top=0,height=600,width=800,toolbar=yes,resizable=yes,status=no,menubar=yes")
  wlexport.document.write("<link rel=stylesheet type=text/css href=./css/amprint.css>"+prnhtml)
  wlexport.document.close()
  
  var mywork=ExcelSheet.workbooks.add;
  var mydoc=mywork.ActiveSheet;
  var sel=wlexport.document.body.createTextRange();
  sel.select();
  wlexport.document.execCommand('Copy');
  sel.moveEnd('character');
  wlexport.close();
  mydoc.paste();
  mydoc.Columns('A:A').ColumnWidth  = 2;
  mydoc.Columns('B:B').ColumnWidth  = 10;
  mydoc.Columns('C:C').ColumnWidth  = 10;
  mydoc.Columns('D:D').ColumnWidth  = 20;
  mydoc.Columns('E:E').ColumnWidth  = 6;
  mydoc.Columns('F:F').ColumnWidth  = 6;
  mydoc.Columns('G:G').ColumnWidth  = 6;
  mydoc.Columns('H:H').ColumnWidth  = 6;
  mydoc.Columns('I:I').ColumnWidth  = 8;
  mydoc.Columns('J:J').ColumnWidth  = 14;
  mydoc.Columns('K:K').ColumnWidth  = 20;
  mydoc.Columns('L:L').ColumnWidth  = 5;
  mydoc.Columns('M:M').ColumnWidth  = 10;
  mydoc.Columns('N:N').ColumnWidth  = 8;
  
  mydoc.Columns('B:B').Font.Underline = false;  
  
  mydoc.Rows('1:1').Font.Name = "\u5B8B\u4F53";  
  mydoc.Rows('1:1').Font.Color = (15,16,27); 
  mydoc.Rows('1:1').Font.Size = 22;  
  mydoc.Rows('1:1').Font.Bold = true; 
  mydoc.Rows('1:1').HorizontalAlignment = -4108;
  mydoc.Rows('1:1').VerticalAlignment = -4108;  
  
  mydoc.Rows('2:3').Font.Name = "\u9ED1\u4F53";
  mydoc.Rows('2:3').Font.Color = (15,16,27); 
  mydoc.Rows('2:3').Font.Size = 8;  
  mydoc.Rows('2:3').Font.Bold = true; 
  mydoc.Rows('2:3').HorizontalAlignment = -4108;
  mydoc.Rows('2:3').VerticalAlignment = -4108;  
  
  mydoc.Rows('4:'+(4+rows-1)).Font.Name = "\u5B8B\u4F53";
  mydoc.Rows('4:'+(4+rows-1)).Font.Color = (15,16,27); 
  mydoc.Rows('4:'+(4+rows-1)).Font.Size = 8;  
  ExcelSheet.Application.Visible = true;
}