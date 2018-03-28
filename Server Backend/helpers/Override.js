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
      status: res.statusCode
    }

    switch (res.statusCode) {
      case 200:
        overallResponse['data'] = data
        break
      case 400:
      case 500:
        error['message'] = 'BAD REQUEST'
        error['code'] = data.code
        error['title'] = data.title.toUpperCase()
        error['description'] = data.desc.toUpperCase()

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
