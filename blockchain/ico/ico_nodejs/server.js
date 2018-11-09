var http = require("http");
var url = require("url");

function start(route) {
  function onRequest(request, response) {

    try {
      var pathname = url.parse(request.url).pathname;
      var params = url.parse(request.url, true).query;
      // console.log("Request for " + request.query.address + " received.");
      console.log ("pathname");
      console.log (pathname);
      console.log ("params");
      console.log (params);

      route(pathname, params, function cb(type, result) {
        response.writeHead(200, { "Content-Type": "text/plain" });
        var json = JSON.stringify(result);

        response.end(json);
        console.log("response done, json   = " + json);
        console.log("response done, result = " + result);
        console.log("response done, type   = " + type);
      });

    } catch (ee) {
      console.log("onRequest error: " + ee);
    }
  }

  http.createServer(onRequest).listen(8888);
  console.log("Server has started.");
}

exports.start = start;

