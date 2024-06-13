const fs = require("fs");
const path = require("path");

const sourceDir = path.join(__dirname, '../backend/aiot/src/main/resources/static/browser');
const targetDir = path.join(__dirname, '../backend/aiot/src/main/resources/static');

function moveFiles(source, target) {
  fs.readdir(source, { withFileTypes: true }, (err, files) => {
    if (err) throw err;

    if (!fs.existsSync(target)) {
      fs.mkdirSync(target, { recursive: true });
    }

    files.forEach(file => {
      const srcPath = path.join(source, file.name);
      const tgtPath = path.join(target, file.name);

      if (file.isDirectory()) {
        moveFiles(srcPath, tgtPath);
      } else {
        fs.rename(srcPath, tgtPath, (err) => {
          if (err) throw err;
        });
      }
    });

    fs.rm(source, { recursive: true }, (err) => {
      if (err) throw err;
    });
  });
}

moveFiles(sourceDir, targetDir);
