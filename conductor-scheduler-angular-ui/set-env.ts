const fs = require('fs');
// Configure Angular `environment.ts` file path
const targetPath = './src/environments/environment.ts';
const targetPathProd = './src/environments/environment.prod.ts';
// Load node modules
const colors = require('colors');
require('dotenv').load();
// `environment.ts` file structure

let WF_SERVER: string = "http://localhost:8080/api/";
let WF_SCHEDULER: string = "http://localhost:1408/api/";

if(undefined != `${process.env.WF_SERVER}` && 'undefined' != `${process.env.WF_SERVER}`)
{
    WF_SERVER = `${process.env.WF_SERVER}`;
}

if(undefined != `${process.env.WF_SCHEDULER}` && 'undefined' != `${process.env.WF_SCHEDULER}`)
{
    WF_SCHEDULER = `${process.env.WF_SCHEDULER}`;
}

const envConfigFile = `export const environment = {
   production: false,
   WF_SERVER: '${WF_SERVER}',
   WF_SCHEDULER: '${WF_SCHEDULER}'
};
`;

const envConfigFileProd = `export const environment = {
    production: true,
    WF_SERVER: '${WF_SERVER}',
    WF_SCHEDULER: '${WF_SCHEDULER}'
 };
 `;

console.log(colors.magenta('The file `environment.ts` will be written with the following content: \n'));
console.log(colors.grey(envConfigFile));
fs.writeFile(targetPath, envConfigFile, function (err) {
   if (err) {
       throw console.error(err);
   } else {
       console.log(colors.magenta(`Angular environment.ts file generated correctly at ${targetPath} \n`));
   }
});

console.log(colors.magenta('The file `environment.prod.ts` will be written with the following content: \n'));
console.log(colors.grey(envConfigFileProd));
fs.writeFile(targetPathProd, envConfigFileProd, function (err) {
   if (err) {
       throw console.error(err);
   } else {
       console.log(colors.magenta(`Angular environment.prod.ts file generated correctly at ${targetPathProd} \n`));
   }
});