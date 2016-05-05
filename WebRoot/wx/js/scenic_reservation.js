$(function(){
	var o = xyz.getUrlparam();
	var nameCn=o.nameCn;
	var currentPrice=o.currentPrice;
	$("#spotName").html(nameCn);
	$("#spotPrice").html(currentPrice);
});