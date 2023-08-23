const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave : false,
  outputDir: '../backend/src/main/resources/static',

  devServer: {
    proxy: {
      "^/api": {
        target: 'http://192.168.150.46:8080',
        changeOrigin: true
      },
    }
  }
})
