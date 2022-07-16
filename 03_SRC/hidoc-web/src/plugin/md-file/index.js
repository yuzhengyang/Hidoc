'use strict';

var _interopRequireDefault = require('@babel/runtime/helpers/interopRequireDefault');

exports.__esModule = true;
exports.default = _default;

var _parser = _interopRequireDefault(require('./parser'));

function _default() {
    return {
        install: function install(VMdEditor) {
            VMdEditor.vMdParser.use(_parser.default);
        }
    };
}
