const HdrRow = (props) => {
  return (
    <thead>
      <tr style={{ marginTop: "10px" }}>
        <th></th>
        <th>Entity</th>
        <th>Group</th>
        <th>Last Run On</th>
        <th>Run By</th>
        <th>Status</th>
        <th>Logs</th>
        <th>Transfer Mode</th>
        <th></th>
      </tr>
    </thead>
  );
};

export default HdrRow;
