package com.example.drawstockview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int count = 0;
    private String[] strings = {"雙鴻 3324 10/19","國泰金 2882 10/20","欣興 3037 10/20","雙鴻 3324 10/20","台積電 2330 10/20"};

    //收盤價
    private String[] closePriceData = {"297.0, 299.5, 299.0, 299.0, 300.0, 301.0, 302.0, 302.0, 302.0, 302.5, 301.5, 301.5, 301.5, 300.0, 300.0, 300.0, 299.5, 299.5, 299.5, 301.0, 299.5, 299.5, 299.5, 299.0, 299.0, 298.0, 298.0, 297.0, 297.5, 297.5, 298.0, 297.5, 297.5, 299.0, 300.0, 300.5, 300.0, 300.0, 300.0, 301.0, 301.0, 301.5, 301.5, 303.5, 303.0, 303.0, 302.5, 302.0, 302.0, 301.5, 302.5, 302.0, 302.0, 302.0, 302.5, 302.5, 302.5, 301.0, 300.5, 300.5, 301.5, 302.0, 302.0, 301.5, 301.0, 300.5, 301.5, 301.5, 301.0, 300.5, 300.5, 300.5, 301.0, 301.0, 301.0, 301.5, 301.5, 302.5, 301.0, 301.5, 301.5, 301.5, 301.5, 302.0, 301.0, 301.5, 301.5, 302.5, 302.5, 302.5, 302.5, 302.5, 302.5, 302.5, 302.5, 302.5, 304.0, 304.0, 304.5, 304.5, 304.0, 303.5, 303.5, 303.0, 302.5, 303.0, 303.0, 303.0, 303.0, 303.0, 302.0, 302.0, 302.0, 302.5, 302.5, 301.5, 301.5, 301.0, 301.0, 301.5, 301.5, 301.5, 302.0, 302.0, 302.0, 301.5, 301.5, 301.5, 301.0, 300.5, 300.5, 300.5, 300.5, 300.5, 301.0, 301.0, 300.5, 300.5, 300.5, 300.5, 300.5, 300.5, 300.5, 300.5, 300.5, 301.0, 301.0, 301.0, 301.0, 302.0, 302.0, 301.0, 301.0, 301.0, 301.5, 300.5, 300.5, 300.0, 300.0, 300.5, 300.5, 300.5, 300.5, 300.0, 300.5, 301.0, 300.5, 300.5, 300.5, 301.0, 300.5, 300.5, 300.0, 300.0, 301.0, 301.0, 301.5, 301.0, 301.5, 301.5, 301.5, 301.5, 301.5, 302.0, 302.0, 302.0, 302.0, 302.0, 302.5, 304.5, 305.5, 304.5, 304.0, 304.5, 304.5, 305.0, 304.5, 304.5, 304.5, 305.0, 306.0, 306.0, 305.0, 305.5, 306.0, 305.5, 305.5, 305.0, 305.0, 304.5, 304.5, 305.0, 305.0, 305.5, 305.5, 306.0, 307.0, 306.5, 307.0, 307.5, 308.0, 308.0, 308.0, 307.5, 308.0, 308.0, 307.5, 308.5",
            "44.25, 44.4, 44.4, 44.45, 44.4, 44.35, 44.3, 44.35, 44.35, 44.3, 44.35, 44.35, 44.3, 44.35, 44.3, 44.3, 44.3, 44.35, 44.3, 44.05, 44.1, 44.15, 44.1, 44.15, 44.15, 44.1, 44.1, 44.0, 44.05, 44.05, 44.0, 44.05, 43.85, 43.85, 43.85, 43.8, 43.8, 43.85, 43.8, 43.8, 43.85, 43.8, 43.85, 43.85, 43.85, 43.85, 43.85, 43.8, 43.95, 44.0, 44.05, 44.15, 44.15, 44.15, 44.1, 44.15, 44.15, 44.1, 44.05, 44.1, 44.1, 44.15, 44.15, 44.15, 44.2, 44.3, 44.3, 44.25, 44.3, 44.35, 44.35, 44.3, 44.3, 44.3, 44.35, 44.3, 44.35, 44.4, 44.45, 44.4, 44.4, 44.4, 44.4, 44.45, 44.4, 44.4, 44.45, 44.4, 44.45, 44.45, 44.45, 44.45, 44.45, 44.4, 44.45, 44.45, 44.45, 44.4, 44.45, 44.45, 44.45, 44.45, 44.45, 44.5, 44.45, 44.5, 44.5, 44.5, 44.5, 44.55, 44.55, 44.55, 44.55, 44.55, 44.6, 44.6, 44.6, 44.6, 44.65, 44.65, 44.75, 44.7, 44.7, 44.75, 44.75, 44.75, 44.75, 44.75, 44.75, 44.7, 44.75, 44.7, 44.75, 44.75, 44.8, 44.7, 44.8, 44.75, 44.7, 44.8, 44.7, 44.75, 44.75, 44.8, 44.75, 44.75, 44.7, 44.7, 44.65, 44.75, 44.75, 44.65, 44.7, 44.7, 44.7, 44.8, 44.75, 44.8, 44.8, 44.8, 44.8, 44.75, 44.75, 44.75, 44.7, 44.75, 44.75, 44.75, 44.75, 44.75, 44.75, 44.8, 44.8, 44.75, 44.8, 44.75, 44.75, 44.8, 44.8, 44.8, 44.8, 44.8, 44.8, 44.8, 44.8, 44.8, 44.8, 44.8, 44.85, 44.8, 44.85, 44.8, 44.8, 44.8, 44.85, 44.8, 44.8, 44.8, 44.8, 44.8, 44.85, 44.8, 44.8, 44.8, 44.8, 44.8, 44.8, 44.85, 44.85, 44.8, 44.85, 44.9, 44.85, 44.85, 44.85, 44.85, 44.85, 44.85, 44.85, 44.85, 44.85, 44.9, 44.9, 44.9, 44.95, 44.9, 44.85, 44.95, 44.9, 44.9, 44.95, 44.95, 44.95, 44.95, 44.9, 44.85, 44.8, 44.8, 44.8, 44.8, 44.8, 44.75, 44.75, 44.75, 44.8, 44.75, 44.7, 44.75, 44.75, 44.75, 44.75, 44.7, 44.75, 44.75, 44.7, 44.75, 44.85, 44.85, 44.85, 44.85, 44.85, 44.85, 44.85, 44.8, 44.75, 44.7",
            "157.5, 158.0, 157.5, 157.5, 157.5, 157.5, 157.0, 156.5, 157.0, 156.5, 157.0, 157.0, 156.5, 156.0, 156.0, 156.0, 156.0, 155.5, 156.0, 156.0, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 157.0, 157.0, 157.0, 157.0, 157.0, 157.5, 157.5, 158.0, 157.5, 158.0, 157.5, 157.5, 158.0, 158.0, 158.0, 157.5, 157.5, 158.0, 157.5, 157.0, 157.5, 157.5, 158.0, 157.5, 157.5, 157.5, 157.5, 157.5, 156.5, 156.5, 156.5, 157.5, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 157.0, 156.5, 156.5, 156.5, 156.5, 156.5, 157.0, 157.0, 156.5, 157.5, 157.0, 157.0, 156.5, 156.5, 156.5, 156.5, 156.5, 157.0, 157.0, 157.0, 156.5, 156.5, 157.0, 157.0, 157.0, 156.5, 157.0, 156.5, 156.5, 156.5, 157.0, 156.5, 157.0, 156.5, 156.5, 157.0, 157.0, 156.5, 156.0, 156.5, 156.5, 157.0, 156.5, 157.0, 157.0, 157.5, 157.0, 157.5, 157.5, 158.0, 158.0, 158.0, 157.5, 157.5, 157.5, 158.0, 157.5, 158.0, 157.5, 157.5, 158.0, 159.0, 159.0, 159.0, 158.5, 158.5, 158.5, 158.5, 158.5, 159.0, 158.5, 158.5, 159.0, 158.5, 158.5, 158.5, 159.0, 159.0, 159.0, 159.0, 159.0, 159.5, 159.0, 159.5, 159.0, 159.0, 159.5, 159.5, 159.0, 159.0, 159.0, 159.5, 159.5, 159.5, 159.5, 159.5, 159.0, 159.0, 159.5, 159.5, 159.5, 159.0, 159.5, 159.0, 159.5, 159.0, 159.5, 159.5, 159.5, 159.5, 159.5, 159.5, 159.0, 159.0, 159.5, 159.5, 159.5, 159.5, 159.0, 159.5, 159.5, 159.0, 159.0, 159.0, 159.0, 159.5, 159.0, 159.5, 159.5, 159.0, 159.0, 159.0, 159.0, 159.0, 159.5, 159.5, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 159.5, 159.5, 159.5, 159.0, 159.5, 159.5, 159.0, 159.5, 159.5, 159.5, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 159.5, 160.0, 160.0, 160.0, 159.5, 159.5, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 161.0",
            "300.0, 303.0, 302.0, 301.5, 303.0, 302.5, 303.0, 302.5, 301.5, 301.5, 302.5, 302.0, 302.0, 302.5, 303.5, 303.0, 304.5, 305.0, 305.0, 308.0, 307.5, 307.5, 307.5, 307.5, 306.5, 306.0, 305.5, 305.0, 305.5, 303.5, 303.0, 303.0, 303.5, 303.5, 304.0, 304.5, 302.5, 301.5, 302.0, 299.0, 299.5, 298.0, 299.0, 299.0, 299.0, 298.5, 299.0, 299.5, 300.0, 299.5, 300.5, 302.0, 301.5, 301.0, 300.0, 300.0, 300.0, 300.0, 300.5, 299.5, 299.5, 299.5, 299.5, 300.0, 300.0, 300.0, 300.0, 300.0, 299.5, 299.0, 299.5, 299.5, 299.5, 300.0, 300.0, 300.0, 299.5, 299.5, 299.5, 299.5, 299.0, 299.0, 298.5, 298.5, 298.0, 298.5, 298.5, 298.5, 299.0, 299.0, 299.0, 299.5, 299.0, 299.0, 299.0, 298.5, 299.0, 300.0, 301.0, 301.5, 301.5, 301.0, 301.0, 301.5, 302.5, 303.0, 303.0, 303.0, 302.5, 303.0, 304.0, 304.5, 305.0, 304.5, 306.0, 306.5, 307.5, 306.5, 306.0, 306.0, 307.0, 307.0, 307.0, 306.5, 305.5, 305.5, 305.5, 305.0, 306.5, 307.5, 308.0, 310.0, 310.0, 309.5, 308.0, 308.0, 307.5, 307.5, 307.0, 307.5, 308.0, 307.5, 308.0, 308.0, 309.0, 310.0, 310.0, 309.0, 309.0, 308.5, 308.0, 309.5, 309.5, 309.0, 309.0, 308.5, 308.5, 308.0, 308.0, 308.0, 308.0, 308.0, 307.5, 307.0, 307.5, 307.5, 307.5, 307.5, 308.5, 308.5, 308.0, 308.5, 308.5, 308.5, 308.0, 308.0, 309.0, 309.0, 309.0, 309.0, 309.0, 310.0, 309.5, 309.0, 310.0, 310.0, 311.0, 311.5, 311.0, 311.0, 312.0, 311.5, 312.0, 312.0, 311.5, 311.5, 311.0, 311.5, 311.0, 311.5, 311.0, 310.0, 310.5, 311.0, 312.5, 312.5, 313.0, 312.5, 312.5, 312.5, 312.5, 312.5, 313.0, 312.5, 312.5, 312.5, 312.0, 312.5, 314.0, 314.0, 313.5, 313.5, 313.5, 313.5, 313.5, 313.5, 313.0, 313.0, 312.5, 313.0, 313.0, 312.0, 312.0, 311.5, 311.0, 312.0, 311.5, 311.5, 312.0, 311.5, 311.5, 311.5, 311.5, 311.5, 311.5, 311.0, 311.5, 311.0, 311.5, 311.5, 311.0, 311.0, 310.5, 310.5, 310.5, 310.5, 310.5, 310.0",
            "549.0, 549.0, 547.0, 548.0, 550.0, 551.0, 551.0, 551.0, 550.0, 550.0, 550.0, 550.0, 549.0, 547.0, 547.0, 548.0, 548.0, 548.0, 548.0, 549.0, 549.0, 549.0, 548.0, 547.0, 547.0, 547.0, 548.0, 547.0, 547.0, 547.0, 548.0, 549.0, 548.0, 549.0, 549.0, 549.0, 549.0, 549.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 549.0, 549.0, 550.0, 550.0, 550.0, 550.0, 551.0, 551.0, 551.0, 551.0, 550.0, 550.0, 550.0, 549.0, 549.0, 550.0, 550.0, 550.0, 550.0, 551.0, 551.0, 550.0, 550.0, 551.0, 550.0, 550.0, 550.0, 551.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 549.0, 550.0, 549.0, 550.0, 549.0, 550.0, 549.0, 549.0, 549.0, 550.0, 549.0, 549.0, 549.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 551.0, 551.0, 552.0, 551.0, 551.0, 551.0, 551.0, 551.0, 551.0, 551.0, 552.0, 551.0, 551.0, 551.0, 551.0, 552.0, 551.0, 551.0, 551.0, 551.0, 550.0, 550.0, 550.0, 550.0, 550.0, 550.0, 551.0, 550.0, 550.0, 551.0, 550.0, 551.0, 551.0, 551.0, 551.0, 551.0, 551.0, 551.0, 552.0, 552.0, 551.0, 551.0, 551.0, 552.0, 551.0, 551.0, 551.0, 551.0, 551.0, 551.0, 552.0, 551.0, 552.0, 553.0, 553.0, 552.0, 552.0, 553.0, 553.0, 552.0, 552.0, 552.0, 552.0, 552.0, 552.0, 552.0, 553.0, 552.0, 552.0, 552.0, 552.0, 553.0, 553.0, 553.0, 552.0, 553.0, 553.0, 553.0, 552.0, 553.0, 552.0, 552.0, 552.0, 552.0, 552.0, 552.0, 553.0, 552.0, 552.0, 552.0, 552.0, 552.0, 553.0, 553.0, 552.0, 552.0, 552.0, 553.0, 552.0, 553.0, 553.0, 553.0, 554.0, 554.0, 554.0, 553.0, 553.0, 554.0, 553.0, 554.0, 553.0, 553.0, 553.0, 553.0, 553.0, 553.0, 553.0, 554.0, 554.0, 554.0, 553.0, 553.0, 553.0, 553.0, 553.0, 553.0, 553.0, 553.0, 553.0, 553.0, 552.0, 552.0, 552.0, 552.0, 552.0, 552.0, 552.0, 553.0, 553.0, 554.0, 554.0, 556.0"};

    //時間
    private String[] hhmmData = {"0901, 0902, 0903, 0904, 0905, 0906, 0907, 0908, 0909, 0910, 0911, 0912, 0913, 0914, 0915, 0916, 0917, 0918, 0919, 0920, 0921, 0922, 0923, 0924, 0925, 0926, 0927, 0928, 0929, 0930, 0931, 0932, 0933, 0934, 0935, 0936, 0937, 0938, 0939, 0940, 0941, 0942, 0943, 0944, 0945, 0946, 0947, 0948, 0949, 0950, 0951, 0952, 0954, 0955, 0956, 0957, 0958, 0959, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1019, 1020, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1032, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1100, 1101, 1102, 1103, 1105, 1106, 1107, 1108, 1111, 1112, 1113, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125, 1126, 1127, 1128, 1131, 1132, 1133, 1134, 1135, 1136, 1137, 1142, 1143, 1144, 1147, 1148, 1149, 1150, 1151, 1152, 1153, 1156, 1157, 1200, 1201, 1202, 1204, 1205, 1206, 1207, 1208, 1211, 1212, 1213, 1216, 1219, 1221, 1225, 1226, 1227, 1228, 1231, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1243, 1244, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258, 1259, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1330",
            "0901, 0902, 0903, 0904, 0905, 0906, 0907, 0908, 0909, 0910, 0911, 0912, 0913, 0914, 0915, 0916, 0917, 0918, 0919, 0920, 0921, 0922, 0923, 0924, 0925, 0926, 0927, 0928, 0929, 0930, 0931, 0932, 0933, 0934, 0935, 0936, 0937, 0938, 0939, 0940, 0941, 0942, 0943, 0944, 0945, 0946, 0947, 0948, 0949, 0950, 0951, 0952, 0953, 0954, 0955, 0956, 0957, 0958, 0959, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 1136, 1137, 1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1151, 1152, 1153, 1154, 1155, 1156, 1157, 1158, 1159, 1200, 1201, 1202, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1243, 1244, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258, 1259, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1330",
            "0901, 0902, 0903, 0904, 0905, 0906, 0907, 0908, 0909, 0910, 0911, 0912, 0913, 0914, 0915, 0916, 0917, 0918, 0919, 0920, 0921, 0922, 0923, 0924, 0925, 0926, 0927, 0928, 0929, 0930, 0931, 0932, 0933, 0934, 0935, 0936, 0937, 0938, 0939, 0940, 0941, 0942, 0943, 0944, 0945, 0946, 0947, 0948, 0949, 0950, 0951, 0952, 0953, 0954, 0955, 0956, 0957, 0958, 0959, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 1136, 1137, 1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1151, 1152, 1153, 1154, 1155, 1156, 1157, 1158, 1159, 1200, 1201, 1202, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1243, 1244, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258, 1259, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1330",
            "0901, 0902, 0903, 0904, 0905, 0906, 0907, 0908, 0909, 0910, 0911, 0912, 0913, 0914, 0915, 0916, 0917, 0918, 0919, 0920, 0921, 0922, 0923, 0924, 0925, 0926, 0927, 0928, 0930, 0931, 0932, 0933, 0934, 0935, 0936, 0937, 0938, 0939, 0940, 0941, 0942, 0943, 0944, 0945, 0946, 0947, 0948, 0949, 0950, 0951, 0952, 0953, 0954, 0955, 0956, 0957, 0958, 1000, 1001, 1002, 1003, 1004, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 1136, 1137, 1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1151, 1152, 1153, 1154, 1155, 1156, 1157, 1159, 1200, 1201, 1202, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1243, 1244, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1253, 1254, 1255, 1256, 1257, 1259, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1330",
            "0901, 0902, 0903, 0904, 0905, 0906, 0907, 0908, 0909, 0910, 0911, 0912, 0913, 0914, 0915, 0916, 0917, 0918, 0919, 0920, 0921, 0922, 0923, 0924, 0925, 0926, 0927, 0928, 0929, 0930, 0931, 0932, 0933, 0934, 0935, 0936, 0937, 0938, 0939, 0940, 0941, 0942, 0943, 0944, 0945, 0946, 0947, 0948, 0949, 0950, 0951, 0952, 0953, 0954, 0955, 0956, 0957, 0958, 0959, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 1136, 1137, 1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1151, 1152, 1153, 1154, 1155, 1156, 1157, 1158, 1159, 1200, 1201, 1202, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1243, 1244, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258, 1259, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1330"};

    //deff計算出來的11條綫的數值
    private String[] linePriceData = {"309.5, 307.3, 305.1, 302.9, 300.7, 298.5, 296.3, 294.1, 291.9, 289.7, 287.5",
            "46.585, 46.288000000000004, 45.991, 45.694, 45.397, 45.1, 44.803000000000004, 44.506, 44.209, 43.912, 43.615",
            "164.95, 163.96, 162.97, 161.98, 160.99, 160.0, 159.01, 158.02, 157.03, 156.04, 155.05",
            "321.15, 318.62, 316.09, 313.56, 311.03, 308.5, 305.97, 303.44, 300.91, 298.38, 295.85",
            "557.0, 554.8, 552.6, 550.4, 548.2, 546.0, 543.8, 541.6, 539.4, 537.2, 535.0"};

    private customDrawStockView customDrawStockView;
    private TextView textView;
    private Button buttonNext,buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customDrawStockView = findViewById(R.id.drawView);
        textView = findViewById(R.id.textView);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        buttonNext.setOnClickListener(new View.OnClickListener() {//下一個
            @Override
            public void onClick(View view) {
                count++;

                if (count == 5) count = 0;

                textView.setText(strings[count]);
                customDrawStockView.drawStockView(closePriceData[count],hhmmData[count],linePriceData[count]);//綫圖繪製
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {//上一個
            @Override
            public void onClick(View view) {
                count--;
                if (count == -1) count = 4;

                textView.setText(strings[count]);
                customDrawStockView.drawStockView(closePriceData[count],hhmmData[count],linePriceData[count]);//綫圖繪製
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        textView.setText(strings[count]);
        customDrawStockView.drawStockView(closePriceData[count],hhmmData[count],linePriceData[count]);//綫圖繪製
    }
}