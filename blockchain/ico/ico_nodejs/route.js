function route(pathname, params, cb1) {
	console.log(pathname);

	try {
		if (pathname == "/checkstatus") {
			cb1("str", "OK");
		} else if (pathname == "/newaddress") {
			var accountsoperation = require("./accountsoperation");
			accountsoper = new accountsoperation();
			accountsoper.newaddress(function cb(result) {
				cb1("str", result);
			});
		} else if (pathname == "/listaddress") {
			console.log(22);
			var accountsoperation = require("./accountsoperation");
			accountsoper = new accountsoperation();

			accountsoper.listaddress(function cb(result) {
				console.log(result);
				cb1('str', result);
			});
		} else if (pathname == "/getEthBalance") {
			console.log(params);
			var ethoperation = require("./ethoperation");
			var ethoper = new ethoperation();
			ethoper.getBalance(params.address, function (result) {
				cb1('str', result);
			});
		} else if (pathname == "/sendTokens") {
			console.log(params);
			var tokenoperation = require("./tokenoperation");
			var tokenoper = new tokenoperation();
			tokenoper.sendtokens (params.name, params.from, params.to, params.value, function (result) {
				cb1('str',result);
			});
		} else if (pathname == "/addcontract") {
			console.log(params);
			var tokenoperation = require("./tokenoperation");
			var tokenoper = new tokenoperation();
			tokenoper.addcontract(params.name, params.abi, params.address, function (result) {
				cb1('str', result);
			});
		} else if (pathname == "/sendEthcoins") {
			console.log(2222);
			var ethoperation = require("./ethoperation");
			var ethoper = new ethoperation();
			ethoper.sendconins(params.to, params.value, function (result) {
				cb1('str', result);
			});
		} else if (pathname == "/getConinBalance") {
			console.log(params);
			var tokenoperation = require("./tokenoperation");
			var tokenoper = new tokenoperation();
			tokenoper.balanceOf(params.name, params.address, function (result) {
				console.log ("getCoinBalance result : " + result);
				cb1('str', result);
			});
		} else if (pathname == "/resendTx") {
			var ethoperation = require("./ethoperation");
			var ethoper = new ethoperation();
			ethoper.resendTx(params.hash, params.gas, params.gasPrice, params.name, function (result) {
				cb1('str', result);
			});
		}

	} catch (err) {
		console.log(err);
	}
}

exports.route = route;
