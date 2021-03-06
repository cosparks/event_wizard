package persistence;

import org.json.JSONObject;

// CITATION: code copied from Writable interface in JsonSerializationDemo
//           GitHub: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
