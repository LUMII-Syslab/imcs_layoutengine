const path = require('path');

module.exports = {

  entry: {
    main: './dist/imcs_layoutengine.nocache.js',
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    library: 'imcs_layoutengine',
    libraryTarget: 'umd',
    filename: 'imcs_layoutengine.min.js'
  }
};
