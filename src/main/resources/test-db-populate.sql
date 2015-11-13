INSERT INTO processes (id, name) VALUES (100, 'go.GraphLinksModel');
INSERT INTO tasks (id, name, category, loc) VALUES (101, "Kookie Brittle", "Comment", "360 -10");
INSERT INTO tasks (id, name, category, loc) VALUES (102, "Start", "Start", "175 0");
INSERT INTO process_task (processId, taskId, position) VALUES (100, 101, -13);
INSERT INTO process_task (processId, taskId, position) VALUES (100, 102, -1);
INSERT INTO task_connections (id, processId, fromId, toId, name, fromConnector, toConnector) VALUES (100, 100, 101, 102, null, 'A', 'C');

-- Integration tests data
INSERT INTO processes (id, name) VALUES (1000, 'Some Process');
INSERT INTO processes (id, name) VALUES (1001, 'Process#0');
INSERT INTO processes (id, name) VALUES (1002, 'Process#1');
INSERT INTO processes (id, name) VALUES (1003, 'Process#2');
INSERT INTO processes (id, name) VALUES (1004, 'Process#3');
INSERT INTO processes (id, name) VALUES (1005, 'Process#4');
INSERT INTO processes (id, name) VALUES (1006, 'Process#5');
INSERT INTO processes (id, name) VALUES (1007, 'Process#6');
INSERT INTO processes (id, name) VALUES (1008, 'Process#7');
INSERT INTO processes (id, name) VALUES (1009, 'Process#8');
INSERT INTO processes (id, name) VALUES (1010, 'Process#9');
