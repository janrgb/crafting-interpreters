#include <stdio.h>
#include <stdlib.h>

/* Define the doubly linked list structure. */
struct list_element {
	struct list_element *next;
	char *content;
	struct list_element *prev;
};

/* Function prototypes. */
void populate (struct list_element **my_list, char **elements, int size);
void insert (struct list_element **my_list, char *insertion, int index);
char* find (struct list_element **my_list, int index);
void delete (struct list_element **my_list, int index);
void print_list (struct list_element *my_list);

/* Main. */
int main (int argc, char **argv) {
	setvbuf(stdout, NULL, _IONBF, 0);
	printf("Main entered.\n");
	struct list_element *doubly_list;

	char initial_chars[20][5] = {
		"four",
		"tort",
		"mage",
		"tart",
		"poop",
		"peep",
		"mold",
		"core",
		"land",
		"mine",
		"love",
		"like",
		"port",
		"lang",
		"plop",
		"pork",
		"gold",
		"ores",
		"pore",
		"pour"
	};

	char *initial_chars_ptr[20];
	for (int i = 0; i < 20; i++) {
		initial_chars_ptr[i] = initial_chars[i];
	}

	printf("Populating list...\n");
	populate(&doubly_list, initial_chars_ptr, 20);
	printf("Done populating list.\n");

	printf("Printing list...\n");
	print_list(doubly_list);
	printf("Done printing list.\n");
	
	printf("Inserting into head of list...\n");
	insert(&doubly_list, "potato", 0);
	printf("Done inserting into head of list.\n");

	printf("Printing list...\n");
	print_list(doubly_list);
	printf("Done printing list.\n");

	printf("Inserting into the middle of list...\n");
	insert(&doubly_list, "late", 4);
	printf("Done inserting into middle of list.\n");

	printf("Printing list...\n");
	print_list(doubly_list);
	printf("Done printing list.\n");

	printf("Deleting from beginning of list...\n");
	delete(&doubly_list, 0);
	printf("Done deleting from beginning.\n");

	printf("Printing list...\n");
	print_list(doubly_list);
	printf("Done printing list.\n");

	printf("Deleting from middle of list...\n");
	delete(&doubly_list, 5);
	printf("Done deleting from middle.\n");

	printf("Printing list..\n");
	print_list(doubly_list);
	printf("Done printing list.\n");

	printf("Deleting from end of list...\n");
	delete(&doubly_list, 19);
	printf("Done deleting from end.\n");

	printf("Printing list..\n");
	print_list(doubly_list);
	printf("Done printing list.\n");

	printf("Finding what is at index 0...\n");
	printf("%s\n", find(&doubly_list, 0));
	printf("Found.\n");

	printf("Finding what is at index 9...\n");
	printf("%s\n", find(&doubly_list, 9));
	printf("Found.\n");

	printf("Finding what is at index 200..\n");
	printf("%s\n", find(&doubly_list, 200));
	printf("not found.\n");

	return 0;
}

/* Populate function. */
void populate (struct list_element **my_list, char **elements, int size) {
	/* Do head node first. */
	struct list_element *head = malloc (sizeof (struct list_element));
	struct list_element *curr = head;
	int idx = 0;

	/* Traverse nodes forward and add links. */
	while (idx < size) {
		curr->content = elements[idx];

		if (idx != size - 1) {
			curr->next = malloc (sizeof(struct list_element));
			curr = curr->next;
		}
		else {
			curr->next = NULL;
		}
		idx++;
	}
	
	curr->next = NULL;
	
	/* Adding the prev links. */
	curr = head;
	curr->prev = NULL;
	struct list_element *curr_next = curr->next;

	while (curr_next) {
		curr_next->prev = curr;
		curr = curr->next;
		curr_next = curr_next->next;
	}

	*my_list = head;
}

void insert (struct list_element **my_list, char *insertion, int index) {
	/* Our insertion strategy depends on where we place our new element. */
	if (index == 0) {
		struct list_element *new_head = malloc (sizeof(struct list_element));
		new_head->prev = NULL;
		new_head->next = *my_list;
		new_head->content = insertion;
		(*my_list)->prev = new_head;
		*my_list = new_head;
	}
	else if (index > 0) {
		int idx = 1;
		struct list_element *curr = *my_list;
		struct list_element *new_element = malloc (sizeof (struct list_element));
		new_element->content = insertion;
		while (curr->next != NULL) {
			if (idx == index) {
				new_element->prev = curr;
				new_element->next = curr->next;
				curr->next->prev = new_element;
				curr->next = new_element;
				return;
			}
			curr = curr->next;
			idx++;
		}

		curr->next = new_element;
		new_element->prev = curr;
		new_element->next = NULL;
	}
	else {
		printf("Invalid insertion index. Not inserted.\n");
	}
}

void delete (struct list_element **my_list, int index)
{
	/* Base case. */
	if (*my_list == NULL)
	{
		printf("List is empty. Cannot delete.\n");
		return;
	}

	/* Our deletion strategy changes depending on where we want to delete from. */
	if (index == 0) {
		struct list_element *temp = *my_list;
		*my_list = (*my_list)->next;

		if (*my_list != NULL) {
			(*my_list)->prev = NULL;
		}
		free(temp);
	}
	else if (index > 0) {
		int idx = 0;
		struct list_element *curr = *my_list;
		
		while (curr != NULL && idx < index) {
			curr = curr->next;
			idx++;
		}

		if (curr != NULL) {
			if (curr->next != NULL) {
				curr->next->prev = curr->prev;
			}

			if (curr->prev != NULL) {
				curr->prev->next = curr->next;
			}

			free(curr);
		} else {
			printf("Index out of range. Not deleted.\n");
		}

		/*
		while (curr->next != NULL) {
			if (idx == index) {
				curr->next->prev = NULL;
				curr->next = curr->next->next;
				curr->next->prev = curr;
				return;
			}
			curr = curr->next;
			idx++;
		}

		curr->prev->next = NULL;
		curr->prev = NULL;
		*/
	}
	else {
		printf("Invalid deletion index. Not deleted.\n");
	}
}

char* find (struct list_element **my_list, int index) {
	if (*my_list == NULL || index < 0) {
		return "Nothing to find.\n";
	}

	struct list_element *curr = *my_list;
	int idx = 0;
	while (curr != NULL && idx < index) {
		curr = curr->next;
		idx++;
	}

	if (curr != NULL) {
		return curr->content;
	}
	else {
		return "Index out of range.\n";
	}
}

void print_list (struct list_element *my_list) {
	struct list_element *curr = malloc (sizeof (struct list_element));
	curr = my_list;

	/* Print forwards. */
	while (curr->next != NULL)
	{
		printf("Element (forward): %s\n", curr->content);
		curr = curr->next;
	}
	printf("Element (forward): %s\n", curr->content);

	/* Print backwards. */
	while (curr != NULL)
	{
		printf("Element (prev): %s\n", curr->content);
		curr = curr->prev;
	}
}
