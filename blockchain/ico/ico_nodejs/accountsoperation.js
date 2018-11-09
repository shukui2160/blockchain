function accountsoperation() {
	var Web3 = require('web3');
	var web3 = new Web3('http://localhost:8545');

	this.newaddress = function (callback) {
		try {
			web3.eth.personal.newAccount().then(function (address) {
				ret = { "errorCode": 0, data: address, "errorStr": '创建账号成功'};

				console.log(JSON.stringify(ret));
				callback(ret);
				return 0;
			});
		} catch (err) {
			ret = { "errorCode": -1, 'data': '', "errorStr": '创建账号失败, error:' + err};
			console.log(JSON.stringify(ret));
			callback(ret);
			return -1;
		}
	};

	this.listaddress = function (callback) {
		try {
			web3.eth.personal.getAccounts().then(function (accounts) {
				ret = { "errorCode": 0, 'data': accounts, "errorStr": '获取账号列表成功'};

				console.log(JSON.stringify(ret));
				callback(ret);
				return 0;
			});
		} catch (err) {
			ret = { "errorCode": -1, 'data': '', "errorStr": '获取账号列表失败, error:' + err};
			console.log(JSON.stringify(ret));
			callback(ret);
			return -1;
		}
	};
};

module.exports = accountsoperation;

