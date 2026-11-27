import { StyleSheet, Text, View } from 'react-native';

export default function DetailRow({ label, value }) {
  return (
    <View style={styles.row}>
      <Text style={styles.label}>{label}</Text>
      <Text style={styles.value}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  row: {
    marginBottom: 12,
  },
  label: {
    fontSize: 12,
    fontWeight: 'bold',
    color: '#555',
  },
  value: {
    fontSize: 16,
    marginTop: 2,
  },
});
