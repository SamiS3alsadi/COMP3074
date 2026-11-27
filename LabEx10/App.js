import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import ContactDetailsScreen from './src/screens/ContactDetailsScreen';
import ContactListScreen from './src/screens/ContactListScreen';

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen
          name="Contacts"
          component={ContactListScreen}
          options={{ title: 'Contacts' }}
        />
        <Stack.Screen
          name="Details"
          component={ContactDetailsScreen}
          options={{ title: 'Contact Details' }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
