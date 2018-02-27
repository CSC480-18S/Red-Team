/**
 * Imports package.json
 */
const packageFile = require('../package.json')

/**
 *
 * @param {Response Object} res - response object to be overriden
 */
function json(res) {
  let json = res.json

  res.json = function(data) {
    let overallResponse = {}

    const metaData = {
      'copyright': 'Copyright ' + new Date().getFullYear() + ' CSC480-18S Red Team.',
      'version': packageFile['version'],
      'license': packageFile['license']
    }
    overallResponse['metadata'] = metaData

    let error = {
      status: res.statusCode,
      code: '',
      title: '',
      description: ''
    }

    switch (res.statusCode) {
      case 200:
        overallResponse['data'] = data
        break
      case 400:
        error['code'] = 'To be added Later'
        error['message'] = 'Bad Request'
        error['code'] = data.code
        error['title'] = data.title
        error['description'] = data.desc

        overallResponse['error'] = error
        break
      case 500:
        error['code'] = 'To be added Later'
        error['message'] = 'Internal Server Error'
        error['code'] = data.code
        error['title'] = data.title
        error['description'] = data.desc

        overallResponse['error'] = error
        break
    }

    json.call(this, overallResponse)
  }
}

/**
 * Exports this file so it can be used by other files.  Keep this at the bottom.
 */
module.exports = {
  json: json
}
