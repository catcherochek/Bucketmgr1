<?php //ОСНОВНОЙ МОДУЛЬ  
	include 'functs.php';
	include 'globls.php';?>
<!DOCTYPE html>
<html lang="uk">
<head>
  <title>
    &#1054;&#1075;&#1083;&#1103;&#1076; | &#1052;&#1110;&#1081; &#1050;&#1080;&#1111;&#1074;&#1089;&#1090;&#1072;&#1088;</title>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content=""/>
  <meta name="keywords" content=""/>
  <meta name="robots" content="index,follow"/>
  <style type="text/css">
  	.itemshow{
  	border: 3px solid black;
  	background-color: yellow;
  	
  	}
  </style> 
</head>
  <body>
  
  <h1>Step # <?php  
  if (isset($_GET['stage'])) { echo $_GET['stage'];} else {echo 0;};
  
  ?></h1>
<?php
if (isset($_GET['stage'])){
	if ($_GET['stage'] == 1){//ПОКАЗЫВАЕТ ВСЕХ ПРОИЗВОДИТЕЛЕЙ
		
		$man_name = (explode('||', $_GET['man'],2)[1]);
		$man_id = (explode('||', $_GET['man'],2)[0]);
		echo '<h1> STAGE 2</h1>';
		echo "<br><p>производитель {$man_name}, ID = {$man_id}</p>"  ;
		$res = GetDBData(GETAUTO($man_id));
		$id = array();
		$brand = array();
		$i=0;
		while (odbc_fetch_row($res))
		{
			$id[] = odbc_result($res,"MOD_ID");
			$r = odbc_result($res, 'BEZ');
			$brand[] = mb_convert_encoding($r, "UTF-8", 'Windows-1251');
		}
		echo "<form action=\"test.php\" method=GET>";
		echo RenderSelects("cartype_id", $id, $brand, "тип  машины");
		echo "<input type=\"hidden\" name=\"stage\" value = 2>";
		echo "<input type=\"hidden\" name=\"man_name\" value = {$man_name}>";
		echo "<input type=\"hidden\" name=\"man_id\" value = {$man_id}>";
		echo "<input type=\"submit\" value=\"OK\"></form>";
		//print_r($_GET);
	}
	if ($_GET['stage'] == 2){
		$id = array();
		$brand = array();
		$cartypeID = explode('||', $_GET['cartype_id'],2)[0];
		$cartypeName = explode('||', $_GET['cartype_id'],2)[1];
		echo '<h1> STAGE 3</h1>';
		echo "<br><p>производитель {$_GET['man_name']}, ID = {$_GET['man_id']}</p>"  ;
		echo "<br><p>тип машины - {$cartypeName}, ID = {$cartypeID}";
		$qr = GETMARKS($cartypeID);
		$res = GetDBData($qr);
		while (odbc_fetch_row($res))
		{
			$id[] = odbc_result($res,"TYP_ID");
			$veh = (odbc_result($res, 'VEHICLE_DES'));
			$body = (odbc_result($res, 'body'));
			$r = "{$veh}({$body})";
			$brand[] = mb_convert_encoding($r, "UTF-8", 'Windows-1251');
		}
		echo "<form action=\"test.php\" method=GET>";
		echo RenderSelects("car_id", $id, $brand, "модель");
		echo "<input type=\"hidden\" name=\"stage\" value = 3>";
		echo "<input type=\"hidden\" name=\"man_name\" value = {$_GET['man_name']}>";
		echo "<input type=\"hidden\" name=\"man_id\" value = {$_GET['man_id']}>";
		echo "<input type=\"hidden\" name=\"cartype_id\" value = {$cartypeID}>";
		echo "<input type=\"hidden\" name=\"cartype_name\" value = {$cartypeName}>";
		echo "<input type=\"submit\" value=\"OK\"></form>";
		//print_r($_GET);
	}
	if ($_GET['stage'] == 3){
		$carID = explode('||', $_GET['car_id'],2)[0];
		$carName = explode('||', $_GET['car_id'],2)[1];
		echo '<h1> STAGE 4</h1>';
		echo "<br><p>производитель {$_GET['man_name']}, ID = {$_GET['man_id']}</p>"  ;
		echo "<br><p>тип машины {$_GET['cartype_name']}, ID = {$_GET['cartype_id']}</p>"  ;
		echo "<br><p>машина - {$carName}, ID = {$carID}</p>";
		echo "<br><p>печатаем дерево</p>";
		$res = GetDBData(GETPARTTREE($carID));
		$arr = array();
		$val = false;
		while ($val=odbc_fetch_array($res))
		{
				
				$arr[]=$val;
				
		}
		for ($i=0;$i<sizeof($arr);$i++){
			
				echo "<div style=\"margin-left:".(($arr[$i]['STR_LEVEL'])*50)."px; border-left: 3px solid black; border-bottom: 3px solid black;\"><p>";
				if (($i+1<sizeof($arr))&&(($arr[$i]['STR_LEVEL']>$arr[$i+1]['STR_LEVEL'])|($arr[$i]['STR_LEVEL']==$arr[$i+1]['STR_LEVEL']))){
					echo "<a href = \"test.php?car_id={$_GET['car_id']}&stage=4&man_name={$_GET['man_name']}&man_id={$_GET['man_id']}&cartype_id={$_GET['cartype_id']}&cartype_name={$_GET['cartype_name']}&cat_id={$arr[$i]['STR_ID']}&cat_name=".mb_convert_encoding($arr[$i]['TEX_TEXT'],"UTF-8", 'Windows-1251')."\">";
				}
				echo mb_convert_encoding($arr[$i]['TEX_TEXT'],"UTF-8", 'Windows-1251').' - '. $arr[$i]['STR_ID'];
				
				if (($i+1<sizeof($arr))&&(($arr[$i]['STR_LEVEL']>$arr[$i+1]['STR_LEVEL'])|($arr[$i]['STR_LEVEL']==$arr[$i+1]['STR_LEVEL']))){
					echo "</a>";
				}
				
				echo "</p></div>";
		 
		}	
	}
	if ($_GET['stage'] == 4){
		$carID = explode('||', $_GET['car_id'],2)[0];
		$carName = explode('||', $_GET['car_id'],2)[1];
		echo '<h1> STAGE 5</h1>';
		echo "<br><p>производитель {$_GET['man_name']}, ID = {$_GET['man_id']}</p>"  ;
		echo "<br><p>тип машины {$_GET['cartype_name']}, ID = {$_GET['cartype_id']}</p>"  ;
		echo "<br><p>машина - {$carName}, ID = {$carID}</p>";
		echo "<br><p>выбраная категория - {$_GET['cat_name']}, ID = {$_GET['cat_id']}</p><br>";
		$res = GetDBData(GETTREEINFO($carID,$_GET['cat_id']));
		while ($val=odbc_fetch_array($res)){
			//echo print_r($val)."<br>";
			
			echo "<a href = \"test.php?car_id={$_GET['car_id']}&stage=5&man_name={$_GET['man_name']}&man_id={$_GET['man_id']}&cartype_id={$_GET['cartype_id']}&cartype_name={$_GET['cartype_name']}&cat_id={$_GET['cat_id']}&cat_name={$_GET['cat_name']}&supp_id={$val['SUP_ID']}&ga_nr={$val['GA_NR']}&supp_name=".mb_convert_encoding($val['SUPPLIER'],"UTF-8", 'Windows-1251')."\">";
			echo " Производитель - ".mb_convert_encoding($val['SUPPLIER'],"UTF-8", 'Windows-1251').", группа товаров - ".mb_convert_encoding($val['MASTERBEZ'],"UTF-8", 'Windows-1251')."</a><br/>";
		}
		
	}
	if ($_GET['stage'] == 5){
		$carID = explode('||', $_GET['car_id'],2)[0];
		$carName = explode('||', $_GET['car_id'],2)[1];
		echo '<h1> STAGE 6</h1>';
		echo "<br><p>производитель {$_GET['man_name']}, ID = {$_GET['man_id']}</p>"  ;
		echo "<br><p>тип машины {$_GET['cartype_name']}, ID = {$_GET['cartype_id']}</p>"  ;
		echo "<br><p>машина - {$carName}, ID = {$carID}</p>";
		echo "<br><p>выбраная категория - {$_GET['cat_name']}, ID = {$_GET['cat_id']}</p><br>";
		echo "<br><p>производитель - {$_GET['supp_name']}, ID = {$_GET['supp_id']}, Grop Articles ID = {$_GET['ga_nr']}</p>";
		$res = GetDBData(GETITEMFROMGROUP($carID,$_GET['supp_id'],$_GET['ga_nr']));
		$temp = $_GET;
		$temp['stage']=6;
		while ($val=odbc_fetch_array($res)){
			echo "<br>";
			//echo print_r($val)."<br>";?>
			
			<div class = "itemshow">
			<p style = "font-size: 30; font-style: italic; font-weight: bold;">
			<?php echo "артикул - <span style=\"color:red;\">".mb_convert_encoding($val['ART_ARTICLE_NR'],"UTF-8", 'Windows-1251')."</span>, деталь - ".mb_convert_encoding($val['GA_ASSEMBLY'],"UTF-8", 'Windows-1251').
						"<a style =\"margin-left:20px;\" href = \"".renderGET("test.php", $temp)."&art_id=".$val['ART_ID']."&la_id=".$val['LA_ID']."\">информэйшн</a><span>    </span>".
						"<a style =\"margin-left:20px;\" href = \"test.php\">аналоги</a>";
			echo "</p></div>";
		}
	}
	if ($_GET['stage'] == 6){
		$carID = explode('||', $_GET['car_id'],2)[0];
		$carName = explode('||', $_GET['car_id'],2)[1];
		echo '<h1> STAGE 7</h1>';
		echo "<br><p>производитель {$_GET['man_name']}, ID = {$_GET['man_id']}</p>"  ;
		echo "<br><p>тип машины {$_GET['cartype_name']}, ID = {$_GET['cartype_id']}</p>"  ;
		echo "<br><p>машина - {$carName}, ID = {$carID}</p>";
		echo "<br><p>выбраная категория - {$_GET['cat_name']}, ID = {$_GET['cat_id']}</p><br>";
		echo "<br><p>производитель - {$_GET['supp_name']}, ID = {$_GET['supp_id']}, Grop Articles ID = {$_GET['ga_nr']} номер артикула = {$_GET['art_id']} </p>";
		$res = GetDBData(GETITEMINFO1($_GET['art_id']));
		while ($val=odbc_fetch_array($res)){
			echo "<p> EAN Number - {$val['EAN_EAN']} </p><br>";
			echo "<p> количество в упаковке - {$val['ART_PACK_UNIT']} </p>";
			//echo print_r($val)."<br>";
		}
		
		
		
		
		//print_r($_GET);
		$res = GetDBData(GETIMAGEINFO($_GET['art_id'], $_GET['la_id']));
		while ($val=odbc_fetch_array($res)){
			
			if (isset($val['GRA_TAB_NR'])){
				//echo print_r($val)."<br>";
				$temp = "";
				//echo"<br>".GETIMAGE($val['GRA_GRD_ID'],$val['GRA_TAB_NR']);
				echo "<img src = \"image1.php?gra_grd_id={$val['GRA_GRD_ID']}&gra_tab_nr={$val['GRA_TAB_NR']}\" style = \"border 3px solid black;\"//>";
				/*$res2 = GetDBData(GETIMAGE($val['GRA_GRD_ID'],$val['GRA_TAB_NR']));
		
				while ($val12 = odbc_fetch_array($res2)){
					//$bla1=odbc_result($res2, 'GRD_GRAPHIC');
					//$bla1 = mysqli_real_escape_string($bla1);
					//odbc_binmode($res2,ODBC_BINMODE_PASSTHRU);
					$temp  =$val12['GRD_GRAPHIC'];
		
					$image = new Imagick();
		
						
					//$im = base_convert($val1['GRD_GRAPHIC'],16,2);
					//print_r(mb_convert_encoding();
					$image->readimageblob($temp);
					$image->setimageformat('jpeg');
					//header('Content-type: image/jpeg');
					//echo $image;
		
		
		
		
		
					//print_r($val);
		
		
				}*/
		
		}
	}
		
		//odbc_close($res);
		//echo "<img src = \"image.php?la_id={$_GET['la_id']}&art_id={$_GET['art_id']}\"//>";  
	}
}
else {
		$id = array();
		$brand = array();
		$res = GetDBData(GETMANUFACTURERS);
		$i=0;
		while (odbc_fetch_row($res))
		{
			$id[] = odbc_result($res,"ID");
			$brand[] = odbc_result($res, 'BRAND');
		}
		echo "<form action=\"test.php\" method=GET>";
		echo RenderSelects("man", $id, $brand, "машину");
		echo "<input type=\"hidden\" name=\"stage\" value = 1>";
		echo "<input type=\"submit\" value=\"OK\"></form>";
}

?>

</body>
</html>