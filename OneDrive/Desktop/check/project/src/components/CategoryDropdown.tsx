import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Grid3X3, ChevronDown } from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

const categories = [
  { label: 'Electronics', value: 'electronics' },
  { label: 'Clothing', value: 'clothing' },
  { label: 'Food', value: 'food' },
  { label: 'Sports', value: 'sports' },
  { label: 'Furniture', value: 'furniture' },
  { label: 'Books', value: 'books' },
  { label: 'Beauty', value: 'beauty' },
];

const CategoryDropdown: React.FC = () => {
  const navigate = useNavigate();

  const handleSelect = (category: string) => {
    navigate(`/category/${category}`);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          className="group flex items-center gap-1 bg-black text-white 
                     hover:bg-white hover:text-black 
                     data-[state=open]:bg-white data-[state=open]:text-black
                     rounded-xl px-4 py-2 text-sm font-medium transition-all duration-200"
        >
          <Grid3X3 className="h-4 w-4 text-white group-hover:text-black group-data-[state=open]:text-black" />
          <span>Categories</span>
          <ChevronDown className="h-4 w-4 text-white group-hover:text-black group-data-[state=open]:text-black transition-transform duration-300 group-data-[state=open]:rotate-180" />
        </Button>
      </DropdownMenuTrigger>

      <DropdownMenuContent
        align="start"
        sideOffset={8}
        className="w-48 bg-white text-black border border-gray-200 shadow-lg rounded-xl p-1"
      >
        {categories.map((cat) => (
          <DropdownMenuItem
            key={cat.value}
            onClick={() => handleSelect(cat.value)}
            className="cursor-pointer rounded-lg px-3 py-2 text-sm hover:bg-black hover:text-white transition-colors"
          >
            {cat.label}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default CategoryDropdown;
