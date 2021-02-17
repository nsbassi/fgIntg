import groovy.json.JsonOutput
import groovyx.gpars.GParsPool
import kong.unirest.Unirest

File file = new File('/Users/nsb/Projects/Active/Sun/SPIL/ams/DataMigration/MKI_EMP.tsv')
List<String> lines = file.readLines()
List headers = lines.first().split('\t')

GParsPool.withPool 10, {
    lines.drop(247).eachParallel { line ->
        def fields = line.split('\t')
        def record = ["PersonNumber" : fields[headers.indexOf('PersonNumber')],
                      "LastName"     : fields[headers.indexOf('LastName')],
                      "FirstName"    : fields[headers.indexOf('FirstName')],
                      "MiddleName"   : fields[headers.indexOf('MiddleName')],
                      "WorkEmail"    : fields[headers.indexOf('WorkEmail')],
                      "LegalEntityId": fields[headers.indexOf('LegalEntityId')],
                      "assignments"  : [
                              [
                                      "AssignmentName": fields[headers.indexOf('AssignmentName')],
                                      "BusinessUnitId": fields[headers.indexOf('BusinessUnitId')],
                                      "JobId"         : fields[headers.indexOf('JobId')]
                              ]
                      ]
        ]
        def response = Unirest.post("https://fa-eqza-test-saasfaprod1.fa.ocs.oraclecloud.com/hcmRestApi/resources/11.13.18.05/emps")
                .basicAuth('navtej.singh.b.bassi@oracle.com', 'Li2g2bwir@dg')
                .header("Effective-Of", "RangeStartDate=2008-12-05")
                .header("duplicatePersonSearchOptionCode", "ORA_NONE")
                .header("Content-Type", "application/json")
                .body(JsonOutput.toJson(record))
                .asString();
        println("${fields[headers.indexOf('PersonNumber')]} -> $response.status")
    }
}
