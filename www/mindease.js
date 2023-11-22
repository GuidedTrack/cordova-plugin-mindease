const exec = require("cordova/exec");

exports.ensurePermission = function (name) {
  return new Promise((resolve, reject) => {
    exec(resolve, reject, "Permission", "ensure", [name]);
  });
};
