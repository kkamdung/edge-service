gradlew = './gradlew'
expected_ref = '$EXPECTED_REF'
if os.name == 'nt':
  gradlew = 'gradlew.bat'
  expected_ref = '%EXPECTED_REF%'

custom_build(
    ref = 'edge-service',
    command = gradlew + ' bootBuildImage --imageName ' + expected_ref,
    deps = ['build.gradle', 'src']
)

k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

k8s_resource('edge-service', port_forwards=['9000'])
