# My device information
```
- OS: macOS (Apple Silicon)
- RAM: 24 GB
- Java: 21
- CPU: Apple M4
```

# Run time
```bash
time java -jar target/ad-performance-aggregator-1.0.0-jar-with-dependencies.jar \
  --input ad_data.csv \
  --output results/
```
output:
```
Processed campaigns: 50
Elapsed time: 4862 ms
java -jar target/ad-performance-aggregator-1.0.0-jar-with-dependencies.jar     4.97s user 0.24s system 105% cpu 4.963 total
```


# Peak memory

```bash
time java -Xmx512m \
  -verbose:gc \
  -jar target/ad-performance-aggregator-1.0.0-jar-with-dependencies.jar \
  --input ad_data.csv \
  --output results/
```

output:
```
[0.004s][info][gc] Using G1
[0.066s][info][gc] GC(0) Pause Young (Normal) (G1 Evacuation Pause) 41M->18M(386M) 0.593ms
[0.100s][info][gc] GC(1) Pause Young (Normal) (G1 Evacuation Pause) 82M->18M(386M) 0.480ms
[0.162s][info][gc] GC(2) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.583ms
[0.217s][info][gc] GC(3) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.544ms
[0.268s][info][gc] GC(4) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.548ms
[0.319s][info][gc] GC(5) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.558ms
[0.370s][info][gc] GC(6) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.532ms
[0.422s][info][gc] GC(7) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.537ms
[0.472s][info][gc] GC(8) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.519ms
[0.521s][info][gc] GC(9) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.556ms
[0.573s][info][gc] GC(10) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.533ms
[0.622s][info][gc] GC(11) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.537ms
[0.672s][info][gc] GC(12) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.585ms
[0.722s][info][gc] GC(13) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.531ms
[0.773s][info][gc] GC(14) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.525ms
[0.822s][info][gc] GC(15) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.521ms
[0.872s][info][gc] GC(16) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.441ms
[0.924s][info][gc] GC(17) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.480ms
[0.973s][info][gc] GC(18) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.495ms
[1.023s][info][gc] GC(19) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.488ms
[1.071s][info][gc] GC(20) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.521ms
[1.123s][info][gc] GC(21) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.491ms
[1.171s][info][gc] GC(22) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.500ms
[1.220s][info][gc] GC(23) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.481ms
[1.272s][info][gc] GC(24) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.495ms
[1.321s][info][gc] GC(25) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.512ms
[1.369s][info][gc] GC(26) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.497ms
[1.418s][info][gc] GC(27) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.507ms
[1.469s][info][gc] GC(28) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.519ms
[1.518s][info][gc] GC(29) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.479ms
[1.567s][info][gc] GC(30) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.498ms
[1.618s][info][gc] GC(31) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.464ms
[1.666s][info][gc] GC(32) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.488ms
[1.715s][info][gc] GC(33) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.495ms
[1.765s][info][gc] GC(34) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.507ms
[1.817s][info][gc] GC(35) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.477ms
[1.866s][info][gc] GC(36) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.490ms
[1.917s][info][gc] GC(37) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.527ms
[1.969s][info][gc] GC(38) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.492ms
[2.019s][info][gc] GC(39) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.494ms
[2.069s][info][gc] GC(40) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.497ms
[2.118s][info][gc] GC(41) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.495ms
[2.170s][info][gc] GC(42) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.499ms
[2.220s][info][gc] GC(43) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.520ms
[2.270s][info][gc] GC(44) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.485ms
[2.323s][info][gc] GC(45) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.511ms
[2.372s][info][gc] GC(46) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.509ms
[2.422s][info][gc] GC(47) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.485ms
[2.472s][info][gc] GC(48) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.514ms
[2.524s][info][gc] GC(49) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.492ms
[2.574s][info][gc] GC(50) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.475ms
[2.624s][info][gc] GC(51) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.476ms
[2.676s][info][gc] GC(52) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.499ms
[2.725s][info][gc] GC(53) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.511ms
[2.773s][info][gc] GC(54) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.492ms
[2.824s][info][gc] GC(55) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.497ms
[2.873s][info][gc] GC(56) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.496ms
[2.923s][info][gc] GC(57) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.508ms
[2.972s][info][gc] GC(58) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.500ms
[3.024s][info][gc] GC(59) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.487ms
[3.073s][info][gc] GC(60) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.492ms
[3.122s][info][gc] GC(61) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.478ms
[3.173s][info][gc] GC(62) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.483ms
[3.222s][info][gc] GC(63) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.522ms
[3.271s][info][gc] GC(64) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.470ms
[3.320s][info][gc] GC(65) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.461ms
[3.371s][info][gc] GC(66) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.474ms
[3.420s][info][gc] GC(67) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.470ms
[3.469s][info][gc] GC(68) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.472ms
[3.522s][info][gc] GC(69) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.495ms
[3.572s][info][gc] GC(70) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.470ms
[3.622s][info][gc] GC(71) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.479ms
[3.672s][info][gc] GC(72) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.479ms
[3.725s][info][gc] GC(73) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.483ms
[3.773s][info][gc] GC(74) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.491ms
[3.822s][info][gc] GC(75) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.488ms
[3.873s][info][gc] GC(76) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.470ms
[3.922s][info][gc] GC(77) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.485ms
[3.972s][info][gc] GC(78) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.492ms
[4.021s][info][gc] GC(79) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.492ms
[4.074s][info][gc] GC(80) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.492ms
[4.124s][info][gc] GC(81) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.501ms
[4.174s][info][gc] GC(82) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.484ms
[4.227s][info][gc] GC(83) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.464ms
[4.276s][info][gc] GC(84) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.494ms
[4.325s][info][gc] GC(85) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.466ms
[4.374s][info][gc] GC(86) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.475ms
[4.426s][info][gc] GC(87) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.517ms
[4.475s][info][gc] GC(88) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.497ms
[4.524s][info][gc] GC(89) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.478ms
[4.575s][info][gc] GC(90) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.476ms
[4.624s][info][gc] GC(91) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.484ms
[4.672s][info][gc] GC(92) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.503ms
[4.721s][info][gc] GC(93) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.469ms
[4.772s][info][gc] GC(94) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.503ms
[4.821s][info][gc] GC(95) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.480ms
[4.872s][info][gc] GC(96) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.471ms
[4.924s][info][gc] GC(97) Pause Young (Normal) (G1 Evacuation Pause) 247M->18M(386M) 0.471ms
Processed campaigns: 50
Elapsed time: 4925 ms
java -Xmx512m -verbose:gc -jar  --input ad_data.csv --output results/  5.21s user 0.23s system 109% cpu 4.972 total
```